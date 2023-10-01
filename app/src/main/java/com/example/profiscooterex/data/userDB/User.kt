package com.example.profiscooterex.data.userDB


data class User(
    val nick: String = "",
    val age: String = "",
    val email: String = ""
)

data class Scooter(
    val batteryAh: String = "",
    val batteryVoltage: String = "",
    val bottomCutOff: String = "",
    val motorWatt: String = "",
    val upperCutOff: String = ""
)

