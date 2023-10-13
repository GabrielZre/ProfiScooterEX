package com.example.profiscooterex.data.userDB


data class User(
    val nick: String = "",
    val age: String = "",
    val email: String = ""
)

data class Scooter(
    var batteryAh: String = "",
    var batteryVoltage: String = "",
    var bottomCutOff: String = "",
    var motorWatt: String = "",
    var upperCutOff: String = ""
)

