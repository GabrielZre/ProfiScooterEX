package com.example.profiscooterex.permissions.bluetooth

import androidx.compose.runtime.MutableState

interface BluetoothChecker {
    val bluetoothState: MutableState<Boolean>
}
