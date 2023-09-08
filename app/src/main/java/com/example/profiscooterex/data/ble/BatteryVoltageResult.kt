package com.example.profiscooterex.data.ble

data class BatteryVoltageResult(
    val batteryVoltage: Float,
    val deviceBatteryVoltage: Float,
    val connectionState: ConnectionState
)