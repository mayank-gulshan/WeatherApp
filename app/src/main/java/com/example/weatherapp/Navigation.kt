package com.example.weatherapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherapp.screens.FirstScreen
import com.example.weatherapp.screens.WeatherScreen


@Composable
fun WeatherAppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.FirstScreen.route
    ) {
        composable(Screens.FirstScreen.route) {
            FirstScreen(navController)
        }
        composable(Screens.SecondScreen.route) {
            WeatherScreen(navController)
        }
    }
}
