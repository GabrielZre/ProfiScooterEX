package com.example.profiscooterex.ui.dashboard

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.profiscooterex.ApplicationViewModel
import com.example.profiscooterex.data.userDB.LocationDetails
import com.example.profiscooterex.location.LocationLiveData
import com.example.profiscooterex.permissions.PermissionsViewModel
import com.example.profiscooterex.ui.destinations.HomeScreenDestination
//import com.example.profiscooterex.ui.destinations.LoginScreenDestination
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.spacing
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashboardTestScreen(
    isLocationEnabled : Boolean,
    accessFineLocationState : PermissionState,
    //location : LocationDetails?,
    navigator: DestinationsNavigator,
    locationVM : ApplicationViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,

    ) {
    var latitude by remember { mutableStateOf("0") }
    var longitude by remember { mutableStateOf("0") }
    val locationLiveData = locationVM.getLocationLiveData()
    val locationObserver = Observer<LocationDetails> { location ->
        // Update the UI with the received location details
        location.let {
            latitude = location.latitude
            longitude = location.longitude
        }
    }
    val spacing = MaterialTheme.spacing

    Row(
        modifier = Modifier
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
            Row() {
                Button(
                    onClick = { locationStartObserver(locationLiveData, lifecycleOwner, locationObserver) },
                    modifier = Modifier
                ) {
                    Text(text = "OnObserver")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = { locationRemoveObserver(locationLiveData, locationObserver) },
                    modifier = Modifier
                ) {
                    Text(text = "OffObserver")
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
                Text(
                    text = "IS_ACCESS_FINE_LOCATION: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (accessFineLocationState.status.isGranted) {
                    Text(text = "yes")
                } else {
                    Text(text = "no")
                }
            }


            Text(
                text = "Current speed:" ,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Latitude: $latitude",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Longitude: $longitude",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            /*location?.let {
                Text(
                    text =  location!!.latitude ,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = location!!.longitude ,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }*/



            if(accessFineLocationState.status.isGranted) {
                /*Button(
                    onClick = { locationVM.startLocationUpdates() },
                    modifier = Modifier
                ) {
                    Text(text = "Start speedometer")
                    Log.d("tag", "LocationUpdatesStarted")
                }*/
            } else
            {
                Button(
                    onClick = { accessFineLocationState.launchPermissionRequest() },
                    modifier = Modifier
                ) {
                    Text(text = "Start speedometer (accompanist)")
                }
            }
            Button(
                onClick = { },
                modifier = Modifier
            ) {
                Text(text = "Start location")
                Log.d("tag", "LocationUpdatesStarted")
            }



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
        }


    }

}

fun locationStartObserver(location: LocationLiveData, lifecycleOwner: LifecycleOwner, locationObserver : Observer<LocationDetails>) {
    location.observe(lifecycleOwner, locationObserver)
}

fun locationRemoveObserver(location: LocationLiveData, locationObserver: Observer<LocationDetails>) {
    location.removeObserver(locationObserver)
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

@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun DashboardTestScreen(permissionsVM : PermissionsViewModel = hiltViewModel(), locationVM : ApplicationViewModel = hiltViewModel(), navigator: DestinationsNavigator) {
    //val location by locationVM.getLocationLiveData().observeAsState()
    DashboardTestScreen(
        isLocationEnabled = permissionsVM.locationChecker.locationState.value,
        accessFineLocationState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION),
        //location = location,
        navigator = navigator
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun DashboardTestScreenPreview() {
    AppTheme {
        Surface {
            DashboardTestScreen(
                isLocationEnabled = false,
                accessFineLocationState = PermissionsStatePreview(),
                //location = LocationDetails("0","0"),
                navigator = EmptyDestinationsNavigator
            )
        }
    }
}
