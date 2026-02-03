package com.example.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.weatherservice
import com.example.weatherapp.data.ForecastDayUI
import com.example.weatherapp.data.LocationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _newcity = MutableStateFlow("")
    val newcity : StateFlow<String> =_newcity
    fun UpdateCity(new: String){
        _newcity.value = new
    }
    private val _iconType = MutableStateFlow("")
    val iconType: StateFlow<String> = _iconType
    // 🔹 Weather State
    private val _weatherType = MutableStateFlow("")
    val weatherType: StateFlow<String> = _weatherType
    private val _forecast = MutableStateFlow<List<ForecastDayUI>>(emptyList())
    val forecast: StateFlow<List<ForecastDayUI>> = _forecast
    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city

    private val _country = MutableStateFlow("")
    val country: StateFlow<String> = _country

    private val _localTime = MutableStateFlow("")
    val localTime: StateFlow<String> = _localTime

    private val _temperature = MutableStateFlow(0.0)
    val temperature: StateFlow<Double> = _temperature

    private val _humidity = MutableStateFlow(0)
    val humidity: StateFlow<Int> = _humidity

    private val _feelsLike = MutableStateFlow(0.0)
    val feelsLike: StateFlow<Double> = _feelsLike

    fun refreshWeatherByLocation(locationData: LocationData) { fetchWeatherByLatLng(
            locationData.Latitude,
            locationData.Longitude
        )
    }

    // 🔹 API CALL
    fun fetchWeatherByLatLng(
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val query = "$latitude,$longitude"

                val response = weatherservice.getForecast(
                    city = query,
                    days = 3
                )

                // 🔹 LOCATION
                _city.value = response.location.name
                _country.value = response.location.country
                _localTime.value = response.location.localtime

                // 🔹 CURRENT WEATHER
                _temperature.value = response.current.temp_c
                _humidity.value = response.current.humidity
                _feelsLike.value = response.current.feelslike_c
                _weatherType.value = response.current.condition.text
                _iconType.value = "https:${response.current.condition.icon}"

                // 🔹 FORECAST
                _forecast.value = response.forecast.forecastday.map { day ->
                    ForecastDayUI(
                        date = day.date,
                        maxTemp = "${day.day.maxtemp_c}°C",
                        minTemp = "${day.day.mintemp_c}°C",
                        condition = day.day.condition.text
                    )
                }

                // 🪵 DEBUG LOG
                println("API SUCCESS → $query")

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value= false
            }
        }
    }
    // 🔹 API CALL BY CITY NAME (SEARCH)
    fun fetchWeatherByCityName() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val cityQuery = _newcity.value.trim()
                if (cityQuery.isEmpty()) return@launch

                val response = weatherservice.getForecast(
                    city = cityQuery,
                    days = 3
                )

                // 🔹 LOCATION
                _city.value = response.location.name
                _country.value = response.location.country
                _localTime.value = response.location.localtime

                // 🔹 CURRENT WEATHER
                _temperature.value = response.current.temp_c
                _humidity.value = response.current.humidity
                _feelsLike.value = response.current.feelslike_c
                _weatherType.value = response.current.condition.text
                _iconType.value = "https:${response.current.condition.icon}"

                // 🔹 FORECAST
                _forecast.value = response.forecast.forecastday.map { day ->
                    ForecastDayUI(
                        date = day.date,
                        maxTemp = "${day.day.maxtemp_c}°C",
                        minTemp = "${day.day.mintemp_c}°C",
                        condition = day.day.condition.text
                    )
                }

                println("CITY SEARCH SUCCESS → $cityQuery")

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value=false
            }
        }
    }



}
