package com.example.profiscooterex.ui.dashboard.components.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery0Bar
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Battery2Bar
import androidx.compose.material.icons.filled.Battery4Bar
import androidx.compose.material.icons.filled.Battery5Bar
import androidx.compose.material.icons.filled.Battery6Bar
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.profiscooterex.data.ble.ConnectionState
import com.example.profiscooterex.data.userDB.Scooter
import com.example.profiscooterex.data.userDB.areFieldsNotEmpty
import com.example.profiscooterex.ui.dashboard.checkBluetoothPermissions
import com.example.profiscooterex.ui.dashboard.requestBluetoothPermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BatteryIcon(bleConnectionState: ConnectionState,
                isBluetoothEnabled: Boolean,
                bluetoothPermissionsState: MultiplePermissionsState,
                requestForBluetooth: () -> Unit,
                initializeBLE: () -> Unit,
                disconnectBLE: () -> Unit,
                batteryPercentage: Float,
                scooterData: Scooter,
                showSnackBar: () -> Unit
) {
    val bluetoothTint = determineBluetoothTint(bleConnectionState, isBluetoothEnabled, bluetoothPermissionsState)
    val batteryIcon =  determineBatteryIcon(bleConnectionState, isBluetoothEnabled, bluetoothPermissionsState, batteryPercentage)
    val batteryTint = determineBatteryColor(bleConnectionState, isBluetoothEnabled, bluetoothPermissionsState, batteryPercentage)


    Box(
        modifier = Modifier
            .width(IntrinsicSize.Min),
        contentAlignment = Alignment.Center
    ) {
        // Outer Icon
        Icon(
            imageVector = batteryIcon,
            contentDescription = "Battery Status",
            tint = batteryTint,
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    if (scooterData.areFieldsNotEmpty()) {
                        batteryOnClick(
                            bleConnectionState,
                            isBluetoothEnabled,
                            bluetoothPermissionsState,
                            requestForBluetooth,
                            initializeBLE,
                            disconnectBLE
                        )
                    } else {
                        showSnackBar()
                    }

                }

        )
        if (bleConnectionState == ConnectionState.Uninitialized ||
            bleConnectionState == ConnectionState.Disconnected ||
            bleConnectionState == ConnectionState.CurrentlyInitializing ||
            !checkBluetoothPermissions(isBluetoothEnabled, bluetoothPermissionsState)
        ) {
            Icon(
                imageVector = Icons.Default.Bluetooth,
                contentDescription = "Bluetooth Status",
                tint = bluetoothTint,
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer(alpha = 0.6f)
            )
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
fun determineBatteryColor(bleConnectionState: ConnectionState, isBluetoothEnabled: Boolean, bluetoothPermissionsState: MultiplePermissionsState, batteryPercentage: Float) : Color {
    return when {
        (bleConnectionState == ConnectionState.Uninitialized ||
                bleConnectionState == ConnectionState.Disconnected ||
                bleConnectionState == ConnectionState.CurrentlyInitializing ||
                !checkBluetoothPermissions(isBluetoothEnabled, bluetoothPermissionsState)) -> Color.LightGray
        batteryPercentage <= 15 -> Color.Red
        batteryPercentage <= 60 -> Color.Yellow
        else -> Color.Green
    }
}
@OptIn(ExperimentalPermissionsApi::class)
fun determineBatteryIcon(bleConnectionState: ConnectionState, isBluetoothEnabled: Boolean, bluetoothPermissionsState: MultiplePermissionsState, batteryPercentage: Float) : ImageVector {
    return when {
        (bleConnectionState == ConnectionState.Uninitialized ||
                bleConnectionState == ConnectionState.Disconnected ||
                bleConnectionState == ConnectionState.CurrentlyInitializing ||
                !checkBluetoothPermissions(isBluetoothEnabled, bluetoothPermissionsState)) -> Icons.Default.BatteryFull
        batteryPercentage <= 15 -> Icons.Default.Battery0Bar
        batteryPercentage <= 30 -> Icons.Default.Battery1Bar
        batteryPercentage <= 45 -> Icons.Default.Battery2Bar
        batteryPercentage <= 60 -> Icons.Default.Battery4Bar
        batteryPercentage <= 75 -> Icons.Default.Battery5Bar
        batteryPercentage <= 85 -> Icons.Default.Battery6Bar
        else -> Icons.Default.BatteryFull
    }
}

@OptIn(ExperimentalPermissionsApi::class)
fun determineBluetoothTint(bleConnectionState: ConnectionState, isBluetoothEnabled: Boolean, bluetoothPermissionsState: MultiplePermissionsState) : Color {
    return if (bleConnectionState == ConnectionState.Uninitialized ||
        bleConnectionState == ConnectionState.Disconnected) {
        if(checkBluetoothPermissions(isBluetoothEnabled, bluetoothPermissionsState)) {
            Color.DarkGray
        } else {
            Color.Red
        }
    } else if(bleConnectionState == ConnectionState.CurrentlyInitializing) {
        Color.Blue
    } else {
        Color.DarkGray
    }
}
@OptIn(ExperimentalPermissionsApi::class)
fun batteryOnClick(bleConnectionState: ConnectionState, isBluetoothEnabled: Boolean, bluetoothPermissionsState: MultiplePermissionsState, requestForBluetooth: () -> Unit, initializeBLE: () -> Unit, disconnectBLE: () -> Unit) {
    if (bleConnectionState == ConnectionState.Uninitialized ||
        bleConnectionState == ConnectionState.Disconnected) {
        if(checkBluetoothPermissions(isBluetoothEnabled, bluetoothPermissionsState)) {
            initializeBLE()
        } else {
            requestBluetoothPermissions(isBluetoothEnabled, requestForBluetooth, bluetoothPermissionsState)
        }
    } else {
        disconnectBLE()
    }
}