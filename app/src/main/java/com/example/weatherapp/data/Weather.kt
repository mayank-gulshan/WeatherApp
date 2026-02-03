package com.example.weatherapp.data


data class WeatherResponse(
    val location:WeatherLocation,   // 👈 ADD THIS

    val current : Current,
    val forecast :Forecast
)

data class LocationData(
    val Latitude: Double,
    val Longitude: Double,
    val city: String
)
data class Current(
    val temp_c: Double,
    val humidity: Int,
    val feelslike_c: Double,
    val condition: Condition
)
data class ForecastDayUI(
    val date: String,
    val maxTemp: String,
    val minTemp: String,
    val condition: String
)

data class Condition(
    val text: String,
    val icon: String
)
data class Forecast(
    val forecastday: List<ForecastDay>
)
data class ForecastDay(
    val date: String,
    val day: Day,
    val hour: List<Hour>   // 👈 ADD THIS
)
data class Hour(
    val time: String,
    val temp_c: Double,
    val condition: Condition
)
data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val condition: Condition
)


data class WeatherLocation(
    val name: String,
    val country: String,
    val localtime: String
)
