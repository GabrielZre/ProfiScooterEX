package com.example.profiscooterex.ui.dashboard

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.profiscooterex.ui.dashboard.components.stopWatch.StopWatch
import com.example.profiscooterex.data.DataViewModel
import com.example.profiscooterex.data.userDB.Scooter
import com.example.profiscooterex.permissions.network.ConnectivityObserver
import com.example.profiscooterex.permissions.network.NetworkConnectivityObserver
import com.example.profiscooterex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel
@Inject constructor(
    application: Application,
    private val dataViewModel: DataViewModel,
    private val batteryVoltageReceiveManager: BatteryVoltageReceiveManager
) : AndroidViewModel(application) {

    private var bleInitializingMessage by mutableStateOf<String?>(null)
    private var bleErrorMessage by mutableStateOf<String?>(null)
    private var batteryVoltage by mutableFloatStateOf(0f)
    private var whEnergyConsumed by mutableFloatStateOf(0f)
    var remainingDistance by mutableFloatStateOf(0f)
        private set
    var efficiencyFactor  by mutableFloatStateOf(0f)
        private set
    val scooterData: Scooter
        get() = dataViewModel.scooterDataState.value

    var batteryPercentage by mutableFloatStateOf(0f)

    var bleConnectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private var startBatteryVoltage: Float? = null

    var distanceTrip by mutableFloatStateOf(0f)
    var currentSpeed by mutableFloatStateOf(0f)
    var averageSpeed by mutableFloatStateOf(0f)

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss")

    var stopWatch = StopWatch()

    var connectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(application.applicationContext)

    private val locationLiveData = LocationLiveData(application, stopWatch)

    private val locationObserver = Observer<LocationDetails> { tripData ->
        tripData.let {
            distanceTrip = tripData.distanceTrip
            currentSpeed = tripData.currentSpeed
            averageSpeed = tripData.averageSpeed
        }
    }
    val locationObserverState: LocationLiveData.LocationState
        get() = locationLiveData.locationObserverState

    private fun subscribeToChanges() {
        viewModelScope.launch {
            batteryVoltageReceiveManager.data.collect{ result ->
                when(result) {
                    is Resource.Success -> {
                        bleConnectionState = result.data.connectionState
                        if(startBatteryVoltage == null && bleConnectionState == ConnectionState.Connected ) {
                            startBatteryVoltage = result.data.batteryVoltage
                        }
                        if(startBatteryVoltage != null) {
                            batteryVoltage = result.data.batteryVoltage
                            whEnergyConsumed = result.data.whEnergyConsumed
                            batteryPercentage = calculateBatteryPercent(result.data.whEnergyConsumed)
                            if(distanceTrip >= 0.1) {
                                remainingDistance = calculateRemainingDistance(whEnergyConsumed, distanceTrip)
                                efficiencyFactor = calculateEfficiencyFactor(calculateMaxDistance(), calculateTheoreticalRange())
                            }
                        }
                        if(bleConnectionState == ConnectionState.Disconnected) {
                            batteryPercentage = 0f
                            remainingDistance = 0f
                            efficiencyFactor = 0f
                        }
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
        batteryPercentage = 0f
        efficiencyFactor = 0f
        bleConnectionState = ConnectionState.Uninitialized
        batteryVoltageReceiveManager.disconnect()
        batteryVoltageReceiveManager.closeConnection()
    }

    fun initializeBLEConnection() {
        if(locationLiveData.locationObserverState != LocationLiveData.LocationState.InActive) {
            restart()
        }
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
        locationLiveData.locationObserverState = LocationLiveData.LocationState.Stopped
    }

    fun calculateBatteryDrain(): Float {
        val batteryDrain = calculateStartBatteryPercent().minus(batteryPercentage)
        return if (batteryDrain < 0f) 0f else batteryDrain
    }

    private fun calculateBatteryPercent(whEnergyConsumed: Float) : Float {
        return (calculateWhLeft(whEnergyConsumed) / calculateOriginalWhCapacity()) * 100
    }

    private fun calculateWhLeft(whEnergyConsumed: Float) : Float {
        val startWhLeft = (calculateOriginalWhCapacity() * (calculateStartBatteryPercent() / 100))
        return startWhLeft - whEnergyConsumed
    }

    private fun calculateStartBatteryPercent(): Float {
        return if(startBatteryVoltage != null) {
            val batteryPercent = ((startBatteryVoltage!! - scooterData.bottomCutOff.toFloat()) * 100) / (scooterData.upperCutOff.toFloat() - scooterData.bottomCutOff.toFloat())
            batteryPercent.coerceIn(0f, 100f)
        } else {
            0f
        }
    }
    private fun calculateOriginalWhCapacity(): Float {
        return (scooterData.batteryAh.toFloat() * scooterData.batteryVoltage.toFloat())
    }

    private fun calculateRemainingDistance(whEnergyConsumed: Float, distanceTravelled: Float): Float {
        return calculateWhLeft(whEnergyConsumed) / whEnergyConsumed * distanceTravelled
    }

    private fun calculateTheoreticalRange(): Float {
        val energyCapacityWh = scooterData.batteryAh.toFloat() * scooterData.batteryVoltage.toFloat()

        val rangeKm = (energyCapacityWh) / (scooterData.motorWatt.toFloat() / 25)
        return rangeKm
    }
    private fun calculateMaxDistance(): Float {
        val constantBatteryWh = scooterData.batteryAh.toFloat() * scooterData.batteryVoltage.toFloat()

        return (constantBatteryWh) / whEnergyConsumed * distanceTrip
    }

    private fun calculateEfficiencyFactor(consumptionBasedMaxRange: Float, theoreticalMaxRange: Float ): Float {
        val efficiencyFactor = if (consumptionBasedMaxRange <= theoreticalMaxRange) {
            (consumptionBasedMaxRange / theoreticalMaxRange) * 100
        } else {
            100f
        }
        return efficiencyFactor.coerceIn(0f, 100f)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveTrip(tripName: String) {
        dataViewModel.sendTripData(
            TripDetails(
                LocalDateTime.now().format(formatter),
                tripName,
                "%.1f".format(distanceTrip),
                "%.1f".format(averageSpeed),
                "%.1f".format(batteryVoltage),
                TimeUnit.MILLISECONDS.toMinutes(stopWatch.timeMillis).toString())
        )
    }
}