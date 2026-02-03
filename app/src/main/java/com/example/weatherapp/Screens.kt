package com.example.weatherapp

sealed class Screens(val route: String) {
    object FirstScreen : Screens("First")
    object SecondScreen : Screens("Second")
}
