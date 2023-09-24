package com.example.profiscooterex.navigation

import com.example.profiscooterex.ui.NavGraph
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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


    //val contentNavGraphStartId = NavGraphs.content.startDestination.
    //val contentNavGraphEndId = NavGraphs.contentNavGraph.endDestination.id
    Scaffold(
        bottomBar = {
            if (currentDestination in NavGraphs.content.destinations) {
                BottomBar(navController)
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

    //if you are using material 2 then you  should use  bottom navigation bar
    BottomNavigation(
        modifier = Modifier.height(55.dp),
        backgroundColor = Color.Black,
    ) {
        BottomBarDestination.values().forEach { destination ->
            //similarly with material 2  use bottom nav item
            NavigationBarItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    Log.d("tag", "Current nav click1: $currentDestination")
                    navController.navigate(destination.direction) {
                        var launchSingleTop = true
                        val navigationRoutes = BottomBarDestination.values()

                        val firstBottomBarDestination = navController.currentBackStack.value
                            .firstOrNull {navBackStackEntry -> checkForDestinations(navigationRoutes, navBackStackEntry) }
                            ?.destination
                        // remove all navigation items from the stack
                        // so only the currently selected screen remains in the stack
                        if (firstBottomBarDestination != null) {
                            popUpTo(firstBottomBarDestination.id) {
                                //inclusive = true
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                        Log.d("tag", "Current nav click2: $currentDestination")
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

fun isCurrentDestinationInContentNavGraph(
    navController: NavController,
    contentNavGraphStartId: Int,
    contentNavGraphEndId: Int
): Boolean {
    val currentDestinationId = navController.currentDestination?.id
    return currentDestinationId != null && currentDestinationId in contentNavGraphStartId..contentNavGraphEndId
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
