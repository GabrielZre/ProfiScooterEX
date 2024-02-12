package com.example.profiscooterex.ui.dashboard

import androidx.compose.ui.graphics.Color

class UiState(
    var speed: String = "",
    var distanceTrip: String = "",
    var averageSpeed: String = "",
    var maxSpeed: String = "-",
    val arcValue: Float = 0f,
    var dashboardStateColor: Color
)
