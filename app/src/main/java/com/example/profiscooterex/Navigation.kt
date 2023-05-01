package com.example.profiscooterex

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.profiscooterex.presentation.home.Home
import com.example.profiscooterex.presentation.login.LoginScreen
import com.example.profiscooterex.presentation.login.LoginViewModel
import com.example.profiscooterex.presentation.login.SignUpScreen
import java.security.AccessController

enum class LoginRoutes{
    SignUp,
    SignIn
}

enum class HomeRoutes{
    Home,
    Detail
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel
) {
    NavHost(
        navController = navController,
        startDestination = LoginRoutes.SignIn.name
    ){
        composable(route = LoginRoutes.SignIn.name){
            LoginScreen(onNavToHomePage = {
                navController.navigate(HomeRoutes.Home.name){
                    launchSingleTop = true
                    popUpTo(route = LoginRoutes.SignIn.name){
                        inclusive = true
                    }
                }
            },
                loginViewModel = loginViewModel

            ) {
                navController.navigate(LoginRoutes.SignUp.name){
                    launchSingleTop = true
                    popUpTo(LoginRoutes.SignIn.name){
                        inclusive = true
                    }
                }
            }
        }

        composable(route = LoginRoutes.SignUp.name){
            SignUpScreen(onNavToHomePage = {
                navController.navigate(HomeRoutes.Home.name){
                    popUpTo(LoginRoutes.SignUp.name){
                       inclusive = true
                    }
                }
            },
                loginViewModel = loginViewModel

            ) {
                navController.navigate(LoginRoutes.SignIn.name)
            }
        }

        composable(route = HomeRoutes.Home.name){
            Home()
        }

    }


}