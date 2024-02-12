package com.example.profiscooterex.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.profiscooterex.R
import com.example.profiscooterex.ui.destinations.DashboardTestScreenDestination
import com.example.profiscooterex.ui.destinations.HomeScreenDestination
import com.example.profiscooterex.ui.destinations.MapScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Home(HomeScreenDestination, Icons.Default.Home, R.string.home),
    Dashboard(DashboardTestScreenDestination, Icons.Default.Speed, R.string.dashboard),
    Map(MapScreenDestination, Icons.Default.MyLocation, R.string.location),
}
