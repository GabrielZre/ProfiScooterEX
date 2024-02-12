package com.example.profiscooterex.data.ble

import com.example.profiscooterex.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface BatteryVoltageReceiveManager {

    val data: MutableSharedFlow<Resource<BatteryVoltageResult>>
    fun reconnect()
    fun disconnect()
    fun startReceiving()
    fun closeConnection()
}
