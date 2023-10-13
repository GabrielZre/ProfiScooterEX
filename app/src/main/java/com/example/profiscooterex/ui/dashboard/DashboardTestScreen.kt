package com.example.profiscooterex.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Battery0Bar
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Battery2Bar
import androidx.compose.material.icons.filled.Battery3Bar
import androidx.compose.material.icons.filled.Battery4Bar
import androidx.compose.material.icons.filled.Battery5Bar
import androidx.compose.material.icons.filled.Battery6Bar
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.profiscooterex.data.ble.ConnectionState
import com.example.profiscooterex.data.userDB.LocationDetails
import com.example.profiscooterex.location.LocationLiveData
import com.example.profiscooterex.navigation.ContentNavGraph
import com.example.profiscooterex.permissions.BluetoothPermissions
import com.example.profiscooterex.permissions.LocationPermission
import com.example.profiscooterex.permissions.PermissionsViewModel
import com.example.profiscooterex.ui.dashboard.components.DashboardSpeedIndicator
import com.example.profiscooterex.ui.dashboard.components.dialogs.TripDialog
import com.example.profiscooterex.ui.dashboard.components.icons.BatteryIcon
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.Dark
import com.example.profiscooterex.ui.theme.DarkColor
import com.example.profiscooterex.ui.theme.DarkColor2
import com.example.profiscooterex.ui.theme.DarkGradient
import com.example.profiscooterex.ui.theme.LightColor
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
import kotlinx.coroutines.launch

enum class DashboardState {
    Ready, Fetching, Active, Stopped, Disabled
}
@OptIn(ExperimentalPermissionsApi::class)
fun determineDashboardState(isLocationEnabled: Boolean, locationPermissionState: PermissionState, locationObserverState: LocationLiveData.LocationState): DashboardState {
    return when {
        checkLocationPermissions(isLocationEnabled, locationPermissionState) && locationObserverState == LocationLiveData.LocationState.Active -> DashboardState.Active
        checkLocationPermissions(isLocationEnabled, locationPermissionState) && locationObserverState == LocationLiveData.LocationState.InActive -> DashboardState.Ready
        checkLocationPermissions(isLocationEnabled, locationPermissionState) && locationObserverState == LocationLiveData.LocationState.Fetching -> DashboardState.Fetching
        !checkLocationPermissions(isLocationEnabled, locationPermissionState)  -> DashboardState.Disabled
        locationObserverState == LocationLiveData.LocationState.Stopped -> DashboardState.Stopped
        else -> DashboardState.Disabled
    }
}

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
    distanceTrip: Float,
    currentSpeed: Float,
    averageSpeed: Float,
    start: () -> Unit,
    stop: () -> Unit,
    restart: () -> Unit,
    initializeBLE: () -> Unit,
    disconnectBLE: () -> Unit,
    bleConnectionState: ConnectionState,
    batteryVoltage: Float,
    deviceBatteryVoltage: Float,
    batteryPercentage: Int,
    distanceTime: String,
    isStopWatchActive: Boolean,
    locationObserverState: LocationLiveData.LocationState
) {

    val spacing = MaterialTheme.spacing
    val coroutineScope = rememberCoroutineScope()
    val openAlertDialog = remember { mutableStateOf(false) }

    val locationState by mutableStateOf(determineDashboardState(isLocationEnabled, locationPermissionState, locationObserverState))
    val onClickDashboard: () -> Unit = {
        coroutineScope.launch {
            if (locationState == DashboardState.Ready || locationState == DashboardState.Stopped) {
                start()
            }
            else if (locationState == DashboardState.Disabled){
                requestLocationPermissions(isLocationEnabled, requestForLocation, locationPermissionState)
            }
            else {
                stop()
            }
        }
    }
    val onLongClickDashboard: () -> Unit = {
        coroutineScope.launch {
            restart()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradient)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 100.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            Button(
                onClick = { openAlertDialog.value = true },
                shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(LightColor)
            ) {
                Row(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = distanceTime,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    VerticalDivider(color = DarkColor)
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        tint = Color.LightGray,
                        contentDescription = "Bluetooth On")

                }
            }
            Row {
                if (isBluetoothEnabled && bluetoothPermissionsState.allPermissionsGranted) {
                    Icon(imageVector = Icons.Default.Bluetooth, tint = Color.Green, contentDescription = "Bluetooth On")
                } else {
                    Icon(imageVector = Icons.Default.BluetoothDisabled, tint = Color.Red, contentDescription = "Bluetooth Off")
                }
            }

            DashboardSpeedIndicator(currentSpeed, averageSpeed, distanceTrip, locationState, onClickDashboard, onLongClickDashboard)
            /*Button(
                onClick = { openAlertDialog.value = true },
                modifier = Modifier
            ) {
                Text(text = "Save Trip")
            }*/

            Row {
                /*Button(
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
                }*/

            }

            /*if(bleConnectionState == ConnectionState.CurrentlyInitializing){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator()

                }
            }
*/
            Column(
                modifier = Modifier
                    .padding(20.dp, 0.dp, 20.dp, 40.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .background(LightColor, shape = RoundedCornerShape(20.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            text =  buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontSize = 30.sp) // Set the font size for deviceBatteryVoltage
                                ) {
                                    append(batteryPercentage.toString())
                                }
                                withStyle(
                                    style = SpanStyle(fontSize = 22.sp) // Set the font size for "KM"
                                ) {
                                    append("%")
                                }
                            },
                            fontSize = 30.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    VerticalDivider(color = DarkColor)
                    BatteryIcon(
                        bleConnectionState = bleConnectionState,
                        isBluetoothEnabled = isBluetoothEnabled,
                        bluetoothPermissionsState = bluetoothPermissionsState,
                        requestForBluetooth = requestForBluetooth,
                        initializeBLE = initializeBLE,
                        disconnectBLE = disconnectBLE,
                        deviceBatteryVoltage = deviceBatteryVoltage,
                        batteryPercentage = batteryPercentage
                    )
                    VerticalDivider(color = DarkColor)
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            text =  buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontSize = 30.sp) // Set the font size for deviceBatteryVoltage
                                ) {
                                    append(deviceBatteryVoltage.toString())
                                }
                                withStyle(
                                    style = SpanStyle(fontSize = 22.sp) // Set the font size for "KM"
                                ) {
                                    append("KM")
                                }
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center

                        )

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
@ContentNavGraph
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
        distanceTrip = dashboardViewModel.distanceTrip,
        currentSpeed = dashboardViewModel.currentSpeed,
        averageSpeed = dashboardViewModel.averageSpeed,
        start = { dashboardViewModel.start(lifecycleOwner) },
        stop = dashboardViewModel::stop,
        restart = dashboardViewModel::restart,
        initializeBLE = dashboardViewModel::initializeBLEConnection,
        disconnectBLE = dashboardViewModel::disconnectBLE,
        bleConnectionState = dashboardViewModel.bleConnectionState,
        batteryVoltage = dashboardViewModel.batteryVoltage,
        deviceBatteryVoltage = dashboardViewModel.deviceBatteryVoltage,
        batteryPercentage = dashboardViewModel.batteryPercentage,
        distanceTime = dashboardViewModel.stopWatch.formattedTime,
        isStopWatchActive = dashboardViewModel.stopWatch.isActive,
        locationObserverState = dashboardViewModel.locationObserverState
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
                distanceTrip = 0f,
                currentSpeed = 0f,
                averageSpeed = 0f,
                start = {},
                stop = {},
                restart = {},
                initializeBLE = {},
                disconnectBLE = {},
                bleConnectionState = ConnectionState.CurrentlyInitializing,
                batteryVoltage = 0f,
                deviceBatteryVoltage = 0f,
                batteryPercentage = 0,
                distanceTime = "00:00:00",
                isStopWatchActive = false,
                locationObserverState = LocationLiveData.LocationState.InActive
            )
        }
    }
}
