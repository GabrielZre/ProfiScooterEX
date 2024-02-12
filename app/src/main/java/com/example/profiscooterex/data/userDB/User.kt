package com.example.profiscooterex.data.userDB

data class User(val nick: String = "", val age: String = "", val email: String = "")

data class Scooter(
    var batteryAh: String = "",
    var batteryVoltage: String = "",
    var bottomCutOff: String = "",
    var motorWatt: String = "",
    var upperCutOff: String = ""
)

data class GpsData(var date: String = "", var latitude: Double = 0.0, var longitude: Double = 0.0)

fun Scooter.areFieldsNotEmpty(): Boolean {
    return listOf(batteryAh, batteryVoltage, bottomCutOff, motorWatt, upperCutOff).all {
        it.isNotEmpty()
    }
}
