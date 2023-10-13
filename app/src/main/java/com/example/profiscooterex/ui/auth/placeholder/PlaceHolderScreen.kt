package com.example.profiscooterex.ui.auth.placeholder

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.R
import com.example.profiscooterex.data.Resource
import com.example.profiscooterex.navigation.AuthNavGraph
import com.example.profiscooterex.ui.auth.AuthViewModel
import com.example.profiscooterex.ui.destinations.HomeScreenDestination
import com.example.profiscooterex.ui.destinations.LoginScreenDestination
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.DarkGradient
import com.google.firebase.auth.FirebaseUser
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AuthNavGraph(start = true)
@Destination
@Composable
fun PlaceHolderScreen(
    viewModel : AuthViewModel? = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradient)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center)
    {
        Image(painterResource(id = R.drawable.profiscooter_logo), contentDescription = "Place Holder")
    }


    LaunchedEffect(Unit) {
        coroutineScope.launch {
            delay(1000)
            if (viewModel?.currentUser == null) {
                navigator.navigate(LoginScreenDestination) {
                    popUpTo(LoginScreenDestination.route) { inclusive = true }
                }
            } else {
                navigator.navigate(HomeScreenDestination) {
                    popUpTo(HomeScreenDestination.route) { inclusive = true }
                }
            }
        }
    }
}
