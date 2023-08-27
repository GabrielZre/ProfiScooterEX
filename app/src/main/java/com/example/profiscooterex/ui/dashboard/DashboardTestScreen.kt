package com.example.profiscooterex.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.ApplicationViewModel
import com.example.profiscooterex.MainActivity
import com.example.profiscooterex.R
import com.example.profiscooterex.data.userDB.DataViewModel
import com.example.profiscooterex.location.LocationLiveData
import com.example.profiscooterex.permissions.PermissionsViewModel
import com.example.profiscooterex.ui.auth.AuthViewModel
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
import kotlinx.coroutines.MainScope

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashboardTestScreen(
    isLocationEnabled: Boolean,
    accessFineLocationState : PermissionState,
    //locations : LocationLiveData,
    navigator: DestinationsNavigator) {
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

            Row() {
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

            if(accessFineLocationState.status.isGranted) {
                Button(
                    onClick = {},
                    modifier = Modifier
                ) {
                    Text(text = "Start speedometer")
                }
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
fun DashboardTestScreen(permissionsVM : PermissionsViewModel = hiltViewModel(), /*locationVM : ApplicationViewModel = hiltViewModel(),*/ navigator: DestinationsNavigator) {
    DashboardTestScreen(
        isLocationEnabled = permissionsVM.locationChecker.locationState.value,
        accessFineLocationState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION),
        //locations = locationVM.getLocationLiveData().observe(),
        navigator = navigator
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun DashboardTestScreenPreview() {
    AppTheme {
        Surface() {
            DashboardTestScreen(
                isLocationEnabled = false,
                accessFineLocationState = PermissionsStatePreview(),
                navigator = EmptyDestinationsNavigator
            )
        }
    }
}
