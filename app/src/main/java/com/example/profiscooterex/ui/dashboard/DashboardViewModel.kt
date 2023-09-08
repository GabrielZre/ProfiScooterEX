package com.example.profiscooterex.ui.dashboard

import android.app.Application
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
import com.example.profiscooterex.location.LocationLiveData
import com.example.profiscooterex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
@Inject constructor(
    application: Application,
    private val batteryVoltageReceiveManager: BatteryVoltageReceiveManager
) : AndroidViewModel(application) {

    var bleInitializingMessage by mutableStateOf<String?>(null)
        private set

    var bleErrorMessage by mutableStateOf<String?>(null)
        private set
    var batteryVoltage by mutableStateOf(0f)
        private set

    var bleConnectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private fun subscribeToChanges() {
        viewModelScope.launch {
            batteryVoltageReceiveManager.data.collect{ result ->
                when(result) {
                    is Resource.Success -> {
                        bleConnectionState = result.data.connectionState
                        batteryVoltage = result.data.batteryVoltage
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
        batteryVoltageReceiveManager.disconnect()
    }
    fun reconnectBLE() {
        batteryVoltageReceiveManager.reconnect()
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


    var distanceTrip by mutableFloatStateOf(0f)
    var currentSpeed by mutableFloatStateOf(0f)
    private val locationLiveData = LocationLiveData(application)


    private val locationObserver = Observer<LocationDetails> { tripData ->
        tripData.let {
            distanceTrip = tripData.distanceTrip
            currentSpeed = tripData.currentSpeed
        }
    }

    fun start(lifecycleOwner : LifecycleOwner) {
        locationStartObserver(locationLiveData, lifecycleOwner, locationObserver)
    }

    fun restart() {
        locationLiveData.resetLocationData()
    }

    fun stop() {
        locationLiveData.removeLocationUpdates()
        locationRemoveObserver(locationLiveData, locationObserver)
    }
}