package com.example.profiscooterex.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.plusAssign
import com.example.profiscooterex.ui.appCurrentDestinationAsState
import com.example.profiscooterex.ui.destinations.Destination
import com.example.profiscooterex.ui.startAppDestination
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.spec.Route


@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@SuppressLint("RestrictedApi")
@Composable
fun SampleScaffold(
    startRoute: Route,
    navController: NavHostController,
    topBar: @Composable (Destination, NavBackStackEntry?) -> Unit,
    bottomBar: @Composable (Destination) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val destination = navController.appCurrentDestinationAsState().value
        ?: startRoute.startAppDestination
    val navBackStackEntry = navController.currentBackStackEntry

    // 👇 only for debugging, you shouldn't use currentBackStack API as it is restricted by annotation
    navController.currentBackStack.collectAsState().value.print()

    val bottomSheetNavigator = rememberBottomSheetNavigator()
    navController.navigatorProvider += bottomSheetNavigator

    // 👇 ModalBottomSheetLayout is only needed if some destination is bottom sheet styled
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(16.dp)
    ) {
        Scaffold(
            topBar = { topBar(destination, navBackStackEntry) },
            bottomBar = { bottomBar(destination) },
            content = content
        )
    }
}

private fun Collection<NavBackStackEntry>.print(prefix: String = "stack") {
    val stack = map { it.destination.route }.toTypedArray().contentToString()
    println("$prefix = $stack")
}