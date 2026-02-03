package com.example.weatherapp.screens

import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.data.WeatherResponse

class WeatherRepository(private val api: WeatherService) {

    suspend fun getWeatherByCity(city: String): WeatherResponse {
        return api.getForecast(
            city = city,
            days = 3
        )
    }
}
