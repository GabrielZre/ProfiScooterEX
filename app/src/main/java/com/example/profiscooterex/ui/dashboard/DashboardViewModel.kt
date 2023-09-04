package com.example.profiscooterex.ui.dashboard

import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.profiscooterex.MainActivity
import com.example.profiscooterex.data.userDB.LocationDetails
import com.example.profiscooterex.location.LocationLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class DashboardViewModel
@Inject constructor(
    application: Application,

) : AndroidViewModel(application) {

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