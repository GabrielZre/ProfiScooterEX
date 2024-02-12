package com.example.profiscooterex.permissions

import androidx.lifecycle.ViewModel
import com.example.profiscooterex.permissions.bluetooth.BluetoothChecker
import com.example.profiscooterex.permissions.location.LocationChecker
import com.example.profiscooterex.permissions.service.RequestServiceListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    val locationChecker: LocationChecker,
    val bluetoothChecker: BluetoothChecker,
    private val requestServiceListener: RequestServiceListener
) : ViewModel() {

    fun requestForLocation() {
        requestServiceListener.notifyLocationListeners()
    }

    fun requestForBluetooth() {
        requestServiceListener.notifyBluetoothListeners()
    }

}