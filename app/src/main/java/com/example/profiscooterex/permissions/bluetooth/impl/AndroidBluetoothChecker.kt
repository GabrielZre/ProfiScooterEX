package com.example.profiscooterex.permissions.bluetooth.impl

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import androidx.compose.runtime.mutableStateOf
import com.example.profiscooterex.permissions.bluetooth.BluetoothChecker
import com.example.profiscooterex.permissions.bluetooth.BluetoothStateBroadcastReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidBluetoothChecker @Inject constructor(@ApplicationContext val context: Context) :
    BluetoothChecker {

    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val adapter: BluetoothAdapter = bluetoothManager.adapter

    override val bluetoothState = mutableStateOf(adapter.isEnabled)

    private val bluetoothIntentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)

    private val bluetoothReceiver = BluetoothStateBroadcastReceiver { bluetoothState.value = it }

    init {
        context.registerReceiver(bluetoothReceiver, bluetoothIntentFilter)
    }
}
