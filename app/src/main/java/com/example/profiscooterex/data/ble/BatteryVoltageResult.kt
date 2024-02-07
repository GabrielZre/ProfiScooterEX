package com.example.profiscooterex.data.ble

data class BatteryVoltageResult(
    val batteryVoltage: Float,
    val whEnergyConsumed: Float,
    val connectionState: ConnectionState
)