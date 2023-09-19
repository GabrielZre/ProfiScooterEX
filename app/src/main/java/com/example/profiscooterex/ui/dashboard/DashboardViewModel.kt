package com.example.profiscooterex.ui.dashboard

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.profiscooterex.data.ble.BatteryVoltageReceiveManager
import com.example.profiscooterex.data.ble.ConnectionState
import com.example.profiscooterex.data.userDB.LocationDetails
import com.example.profiscooterex.data.userDB.TripDetails
import com.example.profiscooterex.location.LocationLiveData
import com.example.profiscooterex.ui.dashboard.stopWatch.StopWatch
import com.example.profiscooterex.data.DataViewModel
import com.example.profiscooterex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.roundToLong


@HiltViewModel
class DashboardViewModel
@Inject constructor(
    application: Application,
    private val dataViewModel: DataViewModel,
    private val batteryVoltageReceiveManager: BatteryVoltageReceiveManager
) : AndroidViewModel(application) {

    var bleInitializingMessage by mutableStateOf<String?>(null)
        private set
    var bleErrorMessage by mutableStateOf<String?>(null)
        private set
    var batteryVoltage by mutableStateOf(0f)
        private set
    var deviceBatteryVoltage by mutableStateOf(0f)
        private set

    var bleConnectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private var startBatteryVoltage: Float? = null

    var distanceTrip by mutableFloatStateOf(0f)
    var currentSpeed by mutableFloatStateOf(0f)
    var averageSpeed by mutableFloatStateOf(0f)

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd ' ' HH:mm")

    var stopWatch = StopWatch()

    private val locationLiveData = LocationLiveData(application, stopWatch)

    private val locationObserver = Observer<LocationDetails> { tripData ->
        tripData.let {
            distanceTrip = tripData.distanceTrip
            currentSpeed = tripData.currentSpeed
            averageSpeed = tripData.averageSpeed
        }
    }

    private fun subscribeToChanges() {
        viewModelScope.launch {
            batteryVoltageReceiveManager.data.collect{ result ->
                when(result) {
                    is Resource.Success -> {
                        if(startBatteryVoltage == null) startBatteryVoltage = result.data.batteryVoltage
                        bleConnectionState = result.data.connectionState
                        batteryVoltage = result.data.batteryVoltage
                        deviceBatteryVoltage = result.data.deviceBatteryVoltage
                    }

                    is Resource.Loading -> {
                        bleInitializingMessage = result.message
                        bleConnectionState = ConnectionState.CurrentlyInitializing
                    }

                    is Resource.Error -> {
                        bleErrorMessage = result.errorMessage
                        bleConnectionState = ConnectionState.Uninitialized
                    }
                }
            }
        }
    }

    fun disconnectBLE() {
        startBatteryVoltage = null
        bleErrorMessage = null
        bleConnectionState = ConnectionState.Uninitialized
        batteryVoltageReceiveManager.disconnect()
        batteryVoltageReceiveManager.closeConnection()
    }

    fun initializeBLEConnection() {
        bleErrorMessage = null
        subscribeToChanges()
        batteryVoltageReceiveManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        batteryVoltageReceiveManager.closeConnection()
    }

    @SuppressLint("NewApi")
    fun start(lifecycleOwner : LifecycleOwner) {
        locationStartObserver(locationLiveData, lifecycleOwner, locationObserver)
    }

    fun restart() {
        locationLiveData.resetLocationData()
        currentSpeed = 0f
        averageSpeed = 0f
        distanceTrip = 0f
        locationLiveData.removeLocationUpdates()
        locationRemoveObserver(locationLiveData, locationObserver)
    }

    fun stop() {
        locationLiveData.removeLocationUpdates()
        locationRemoveObserver(locationLiveData, locationObserver)
    }

    fun calculateBatteryDrain(): Float{
        val batteryDrain = startBatteryVoltage?.minus(batteryVoltage) ?: 0f
        return if (batteryDrain < 0f) 0f else batteryDrain
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveTrip(tripName: String) {
        dataViewModel.sendTripData(
            TripDetails(
                LocalDate.now().format(formatter),
                tripName,
                distanceTrip.toString(),
                averageSpeed.toString(),
                batteryVoltage.toString(),
                stopWatch.formattedTime)
        )
    }
}