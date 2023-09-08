package com.example.profiscooterex.permissions

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.profiscooterex.permissions.bluetooth.BluetoothChecker
import com.example.profiscooterex.permissions.location.LocationChecker
import com.example.profiscooterex.permissions.service.RequestServiceListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    val locationChecker: LocationChecker,
    val bluetoothChecker: BluetoothChecker,
    private val requestServiceListener: RequestServiceListener
) : ViewModel() {

    fun requestForLocation() {
        requestServiceListener.notifyLocationListeners()
        Log.d("tag", "Called RFBFROM SVM")
    }

    fun requestForBluetooth() {
        requestServiceListener.notifyBluetoothListeners()
        Log.d("tag", "Called RFBFROM SVM")
    }

}