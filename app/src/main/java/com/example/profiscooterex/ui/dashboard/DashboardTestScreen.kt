package com.example.profiscooterex.ui.dashboard

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.profiscooterex.data.ble.ConnectionState
import com.example.profiscooterex.data.userDB.LocationDetails
import com.example.profiscooterex.location.LocationLiveData
import com.example.profiscooterex.permissions.BluetoothPermissions
import com.example.profiscooterex.permissions.LocationPermission
import com.example.profiscooterex.permissions.PermissionsViewModel
import com.example.profiscooterex.ui.dashboard.components.DashboardSpeedIndicator
import com.example.profiscooterex.ui.dashboard.components.dialogs.TripDialog
import com.example.profiscooterex.ui.destinations.HomeScreenDestination
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.DarkGradient
import com.example.profiscooterex.ui.theme.spacing
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashboardTestScreen(
    isLocationEnabled : Boolean,
    isBluetoothEnabled : Boolean,
    locationPermissionState : PermissionState,
    bluetoothPermissionsState : MultiplePermissionsState,
    requestForLocation: () -> Unit,
    requestForBluetooth: () -> Unit,
    navigator: DestinationsNavigator,
    distanceTrip: Float,
    currentSpeed: Float,
    averageSpeed: Float,
    start: () -> Unit,
    stop: () -> Unit,
    restart: () -> Unit,
    initializeBLE: () -> Unit,
    disconnectBLE: () -> Unit,
    bleConnectionState: ConnectionState,
    bleInitializingMessage: String?,
    bleErrorMessage: String?,
    batteryVoltage: Float,
    deviceBatteryVoltage: Float,
    distanceTime: String,
    isStopWatchActive: Boolean
) {

    val spacing = MaterialTheme.spacing
    val coroutineScope = rememberCoroutineScope()
    val openAlertDialog = mutableStateOf(false)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradient)
            .wrapContentHeight()
            .padding(spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(
            space = 100.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (!isStopWatchActive) {
                                if(checkLocationPermissions(isLocationEnabled, locationPermissionState)) {
                                    start()
                                } else {
                                    requestLocationPermissions(isLocationEnabled, requestForLocation, locationPermissionState)
                                }
                            } else {
                                stop()
                            }
                        }
                    },
                    modifier =  Modifier
                ) {
                    Text(text = if (isStopWatchActive) "Pause" else "Start")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = restart,
                    modifier = Modifier
                ) {
                    Text(text = "Restart")
                }
            }
            Row {
                Text(
                    text = "Location enabled: ",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isLocationEnabled) {
                    Text(text = "yes")
                } else {
                    Text(text = "no")
                }
            }
            Row {
                if (!locationPermissionState.status.isGranted) {
                    Text(text = "Location permissions NO")
                } else {
                    Text(text = "Location permissions OK")
                }
            }
            Row {
                Text(
                    text = "Bluetooth enabled: ",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isBluetoothEnabled) {
                    Text(text = "yes")
                } else {
                    Text(text = "no")
                }
            }
            Row {
                if (!bluetoothPermissionsState.allPermissionsGranted) {
                    Text(text = "Bluetooth permissions NO")
                } else {
                    Text(text = "Bluetooth permissions OK")
                }
            }

            Row {
                Text(
                    text = "IS_ACCESS_FINE_LOCATION: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (locationPermissionState.status.isGranted) {
                    Text(text = "yes")
                } else {
                    Text(text = "no")
                }
            }

            Text(
                text = "Current speed: $currentSpeed" ,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Average speed: $averageSpeed" ,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )


            Text(
                text = "Trip: $distanceTrip" ,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Distance time: $distanceTime" ,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            DashboardSpeedIndicator()
            Button(
                onClick = {
                    navigator.navigate(HomeScreenDestination) {
                        popUpTo(HomeScreenDestination.route) {inclusive = true}
                    }
                },
                modifier = Modifier
            ) {
                Text(text = "Home")
            }

            Button(
                onClick = { openAlertDialog.value = true },
                modifier = Modifier
            ) {
                Text(text = "Save Trip")
            }

            Row {
                Button(
                    onClick = {
                        coroutineScope.launch {
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
                    },
                    modifier = Modifier
                ) {
                    Text(text = if (bleConnectionState == ConnectionState.Connected) "Disconnect"
                        else if (bleConnectionState == ConnectionState.CurrentlyInitializing) "Initializing"
                        else "Init"
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = disconnectBLE,
                    modifier = Modifier
                ) {
                    Text(text = "disconnect")
                }

            }






            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if(bleConnectionState == ConnectionState.CurrentlyInitializing){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        CircularProgressIndicator()
                        if(bleInitializingMessage != null){
                            Text(
                                text = bleInitializingMessage!!
                            )
                        }
                    }
                    Log.d("tag", "Initializing")
                }else if(!bluetoothPermissionsState.allPermissionsGranted){
                    Text(
                        text = "Go to the app setting and allow the missing permissions.",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                    Log.d("tag", "Permissions OK")
                }else if(bleErrorMessage != null){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = bleErrorMessage!!
                        )
                        Button(
                            onClick = {
                                if(bluetoothPermissionsState.allPermissionsGranted){
                                    initializeBLE()
                                }
                            }
                        ) {
                            Text(
                                "Try again"
                            )
                        }
                    }
                    Log.d("tag", "Error not connected")
                }else if(bleConnectionState == ConnectionState.Connected){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = "Battery Voltage: $batteryVoltage",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Device Battery: $deviceBatteryVoltage",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Log.d("tag", "OK")
                }else if(bleConnectionState == ConnectionState.Disconnected){
                    Button(onClick = {
                        initializeBLE()
                    }) {
                        Text("Initialize again")
                    }
                    Log.d("tag", "Initialize again")
                }
            }
        }
    }

    TripDialog(openAlertDialog)
    if((!isLocationEnabled || !locationPermissionState.status.isGranted) && isStopWatchActive) {
        stop()
        requestLocationPermissions(isLocationEnabled, requestForLocation, locationPermissionState)
    }
}


fun locationStartObserver(location: LocationLiveData, lifecycleOwner: LifecycleOwner, locationObserver : Observer<LocationDetails>) {
    location.observe(lifecycleOwner, locationObserver)
}

fun locationRemoveObserver(location: LocationLiveData, locationObserver: Observer<LocationDetails>) {
    location.removeObserver(locationObserver)
}

@OptIn(ExperimentalPermissionsApi::class)
fun checkLocationPermissions(isLocationEnabled: Boolean, locationPermissionState: PermissionState): Boolean {
    Log.d("tag", "$isLocationEnabled permissions: ${locationPermissionState.status.isGranted}")
    return (isLocationEnabled && locationPermissionState.status.isGranted)
}

@OptIn(ExperimentalPermissionsApi::class)
fun checkBluetoothPermissions(isBluetoothEnabled: Boolean, bluetoothPermissionsState: MultiplePermissionsState): Boolean {
    return (isBluetoothEnabled && bluetoothPermissionsState.allPermissionsGranted)
}

@OptIn(ExperimentalPermissionsApi::class)
fun requestLocationPermissions(isLocationEnabled: Boolean, requestForLocation: () -> Unit, locationPermissionState: PermissionState) {
    if(!locationPermissionState.status.isGranted) {
        locationPermissionState.launchPermissionRequest()
    }
    if(!isLocationEnabled) {
        requestForLocation()
    }
    Log.d("tag", "XXXXXXXX")
}
@OptIn(ExperimentalPermissionsApi::class)
fun requestBluetoothPermissions(isBluetoothEnabled: Boolean, requestForBluetooth: () -> Unit, bluetoothPermissionState: MultiplePermissionsState) {
    if(!isBluetoothEnabled) {
        requestForBluetooth()
    }
    if(!bluetoothPermissionState.allPermissionsGranted) {
        bluetoothPermissionState.launchMultiplePermissionRequest()
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun DashboardTestScreen(permissionsVM : PermissionsViewModel = hiltViewModel(),
                        dashboardViewModel : DashboardViewModel = hiltViewModel(),
                        navigator: DestinationsNavigator) {

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val bluetoothPermissionsState = rememberMultiplePermissionsState(permissions = BluetoothPermissions.permissions)
    val locationPermissionState = rememberPermissionState(permission = LocationPermission.permission)


    DashboardTestScreen(
        isLocationEnabled = permissionsVM.locationChecker.locationState.value,
        isBluetoothEnabled = permissionsVM.bluetoothChecker.bluetoothState.value,
        locationPermissionState = locationPermissionState,
        bluetoothPermissionsState = bluetoothPermissionsState,
        requestForLocation = permissionsVM::requestForLocation,
        requestForBluetooth = permissionsVM::requestForBluetooth,
        navigator = navigator,
        distanceTrip = dashboardViewModel.distanceTrip,
        currentSpeed = dashboardViewModel.currentSpeed,
        averageSpeed = dashboardViewModel.averageSpeed,
        start = { dashboardViewModel.start(lifecycleOwner) },
        stop = dashboardViewModel::stop,
        restart = dashboardViewModel::restart,
        initializeBLE = dashboardViewModel::initializeBLEConnection,
        disconnectBLE = dashboardViewModel::disconnectBLE,
        bleConnectionState = dashboardViewModel.bleConnectionState,
        bleInitializingMessage = dashboardViewModel.bleInitializingMessage,
        bleErrorMessage = dashboardViewModel.bleErrorMessage,
        batteryVoltage = dashboardViewModel.batteryVoltage,
        deviceBatteryVoltage = dashboardViewModel.deviceBatteryVoltage,
        distanceTime = dashboardViewModel.stopWatch.formattedTime,
        isStopWatchActive = dashboardViewModel.stopWatch.isActive,
    )
}








@ExperimentalPermissionsApi
class PermissionsStatePreview : PermissionState {
    override val permission: String
        get() = android.Manifest.permission.ACCESS_FINE_LOCATION
    override val status: PermissionStatus
        get() = PermissionStatus.Denied(shouldShowRationale = true)

    override fun launchPermissionRequest() {
    }
}

@ExperimentalPermissionsApi
class MultiplePermissionsStatePreview : MultiplePermissionsState {
    override val allPermissionsGranted: Boolean
        get() = false
    override val permissions: List<PermissionState>
        get() = permissions
    override val revokedPermissions: List<PermissionState>
        get() = permissions
    override val shouldShowRationale: Boolean
        get() = false

    override fun launchMultiplePermissionRequest() {
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun DashboardTestScreenPreview() {
    AppTheme {
        Surface {
            DashboardTestScreen(
                isLocationEnabled = false,
                isBluetoothEnabled = false,
                locationPermissionState = PermissionsStatePreview(),
                bluetoothPermissionsState = MultiplePermissionsStatePreview(),
                requestForLocation = {},
                requestForBluetooth = {},
                navigator = EmptyDestinationsNavigator,
                distanceTrip = 0f,
                currentSpeed = 0f,
                averageSpeed = 0f,
                start = {},
                stop = {},
                restart = {},
                initializeBLE = {},
                disconnectBLE = {},
                bleConnectionState = ConnectionState.CurrentlyInitializing,
                bleInitializingMessage = "Initializing...",
                bleErrorMessage = "Error...",
                batteryVoltage = 2f,
                deviceBatteryVoltage = 2f,
                distanceTime = "",
                isStopWatchActive = false
            )
        }
    }
}
