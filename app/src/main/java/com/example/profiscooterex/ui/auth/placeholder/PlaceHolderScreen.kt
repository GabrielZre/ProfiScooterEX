package com.example.profiscooterex.ui.auth.placeholder

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.R
import com.example.profiscooterex.navigation.AuthNavGraph
import com.example.profiscooterex.ui.auth.AuthViewModel
import com.example.profiscooterex.ui.destinations.HomeScreenDestination
import com.example.profiscooterex.ui.destinations.LoginScreenDestination
import com.example.profiscooterex.ui.theme.DarkGradient
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
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
        val scaleState by rememberInfiniteTransition(label = "").animateFloat(
            initialValue = 3.5f,
            targetValue = 0.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1100, easing = LinearEasing)
            ), label = ""
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profiscooter_logo),
                contentDescription = "Placeholder",
                modifier = Modifier
                    .scale(scaleState)
                    .align(Alignment.Center)
            )
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            delay(1000)
            if (viewModel?.currentUser == null) {
                navigator.popBackStack()
                navigator.navigate(LoginScreenDestination) {
                    popUpTo(LoginScreenDestination.route) { inclusive = true }
                }
            } else {
                navigator.popBackStack()
                navigator.navigate(HomeScreenDestination) {
                    popUpTo(HomeScreenDestination.route) { inclusive = true }
                }
            }
        }
    }
}
