package com.example.profiscooterex.navigation

import com.example.profiscooterex.ui.NavGraph
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphNavigator
import androidx.navigation.compose.rememberNavController

import com.example.profiscooterex.navigation.BottomBarDestination
import com.example.profiscooterex.ui.NavGraphs
import com.example.profiscooterex.ui.appCurrentDestinationAsState
import com.example.profiscooterex.ui.dashboard.DashboardTestScreen
import com.example.profiscooterex.ui.destinations.Destination
import com.example.profiscooterex.ui.destinations.ScooterSettingsScreenDestination
import com.example.profiscooterex.ui.startAppDestination
import com.example.profiscooterex.ui.theme.AppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.allDestinations
import com.ramcosta.composedestinations.utils.navGraph
import com.ramcosta.composedestinations.utils.startDestination


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Parent() {
    val navController = rememberNavController()
    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    Scaffold(
        bottomBar = {
            if (currentDestination in NavGraphs.content.destinations ||
                currentDestination.route == ScooterSettingsScreenDestination.route) {
                BottomBar(navController)
            }
        },
        topBar = {
            if (currentDestination.route == ScooterSettingsScreenDestination.route) {
                BackArrow(navController)
            }
        }

    ) {
        DestinationsNavHost(
            navController = navController,
            navGraph = NavGraphs.root
        )
    }
}

@SuppressLint("ResourceType")
@Composable
fun BottomBar(
    navController: NavController
) {

    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    BottomNavigation(
        modifier = Modifier.height(55.dp),
        backgroundColor = Color.Black,
    ) {
        BottomBarDestination.values().forEach { destination ->
            NavigationBarItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.navigate(destination.direction) {
                        launchSingleTop = true
                        val navigationRoutes = BottomBarDestination.values()

                        val firstBottomBarDestination = navController.currentBackStack.value
                            .firstOrNull {navBackStackEntry -> checkForDestinations(navigationRoutes, navBackStackEntry) }
                            ?.destination


                        if (firstBottomBarDestination != null) {
                            if(!BottomBarDestination.values().any { it.direction == currentDestination }) {
                                navController.popBackStack()
                            }
                            popUpTo(firstBottomBarDestination.id) {
                                inclusive = true
                                saveState = true
                            }
                            restoreState = true

                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.label),
                        modifier = Modifier
                            .size(if (currentDestination == destination.direction) 30.dp else 25.dp),
                        tint = (if (currentDestination == destination.direction) Color.Black else Color.White)

                    )
                },
            )
        }
    }
}




@SuppressLint("ResourceType")
@Composable
fun BackArrow(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
            .padding(start = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back Arrow",
            modifier = Modifier.clickable {
                navController.popBackStack()
            },
            tint = Color.LightGray
        )
    }

}





fun checkForDestinations(
    navigationRoutes: Array<BottomBarDestination>,
    navBackStackEntry: NavBackStackEntry
): Boolean {
    navigationRoutes.forEach {
        if (it.direction.route == navBackStackEntry.destination.route){
            return  true
        }

    }
    return false
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun ParentPreview() {
    AppTheme {
        Surface {
            BottomBar(navController = rememberNavController())
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun TopBarPreview() {
    AppTheme {
        Surface {
            BackArrow(navController = rememberNavController())
        }
    }
}

