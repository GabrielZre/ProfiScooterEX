package com.example.profiscooterex.permissions.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothStateBroadcastReceiver(private val onStateChanged: (Boolean) -> Unit) :
BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action

        if(action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            when(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.STATE_OFF -> {
                    onStateChanged.invoke(false)
                }
                BluetoothAdapter.STATE_TURNING_OFF -> {
                    onStateChanged.invoke(false)
                }
                BluetoothAdapter.STATE_ON -> {
                    onStateChanged.invoke(true)
                }
                BluetoothAdapter.STATE_TURNING_ON -> {
                    onStateChanged.invoke(true)
                }
            }
        }
    }

}