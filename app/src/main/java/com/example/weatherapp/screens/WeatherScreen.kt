package com.example.weatherapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.LocationUtils

/* -------------------------------------------------- */
/* ---------------- WEATHER UI ---------------------- */
/* -------------------------------------------------- */

@Composable
fun WeatherScreenUI(
    city: String,
    country: String,
    localtime: String,
    temp: String,
    humidity: String,
    feelsLike: String,
    weatherType: String,
    weatherIconRes: Int,
    onRefreshClick: () -> Unit,
    onSearchClick: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(weatherIconRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {

            /* -------- TOP BAR -------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = city,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onRefreshClick) {
                    Icon(Icons.Default.Refresh, null, tint = Color.White)
                }

                IconButton(onClick = onSearchClick) {
                    Icon(Icons.Default.Search, null, tint = Color.White)
                }
            }

            Text(
                text = "$country • $localtime",
                modifier = Modifier.padding(start = 15.dp),
                fontSize = 18.sp,
                color = Color.White
            )

            /* -------- TEMP -------- */
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 100.dp) ,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = temp,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


            /* -------- WEATHER STATS -------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                WeatherStatItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.ic_2),
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    title = "WEATHER",
                    value = weatherType
                )

                WeatherVerticalDivider()
                Spacer(modifier = Modifier.width(2.dp))

                WeatherStatItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.ic_3),
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    title = "FEELS LIKE",
                    value = feelsLike
                )

                WeatherVerticalDivider()

                WeatherStatItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.hum),
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    title = "HUMIDITY",
                    value = "$humidity%"
                )
            }
        }
    }
}

/* -------------------------------------------------- */
/* ---------------- MAIN SCREEN --------------------- */
/* -------------------------------------------------- */

@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val locationUtils = remember { LocationUtils(context) }
    val isLoading by viewModel.isLoading.collectAsState()

    var showSearchDialog by remember { mutableStateOf(false) }

    val city by viewModel.city.collectAsState()
    val country by viewModel.country.collectAsState()
    val localTime by viewModel.localTime.collectAsState()
    val temp by viewModel.temperature.collectAsState()
    val humidity by viewModel.humidity.collectAsState()
    val feelsLike by viewModel.feelsLike.collectAsState()
    val weatherType by viewModel.weatherType.collectAsState()
    val iconType by viewModel.iconType.collectAsState()

    LaunchedEffect(Unit) {
        if (locationUtils.hasLocationPermission()) {
            locationUtils.requestLocationUpdate {
                viewModel.fetchWeatherByLatLng(it.Latitude, it.Longitude)
            }
        }
    }

    fun isNight(time: String): Boolean {
        return try {
            val hour = time.substring(11, 13).toInt()
            hour >= 18 || hour <= 5
        } catch (e: Exception) {
            false
        }
    }


    val night = isNight(localTime)

    val weatherIconRes = when {
        weatherType.contains("rain", true) -> R.drawable.ic_rainy
        weatherType.contains("cloud", true) -> R.drawable.ic_rainy
        night -> R.drawable.ic_night
        else -> R.drawable.ic_clear
    }

    WeatherScreenUI(
        city = city,
        country = country,
        localtime = localTime,
        temp = "${temp.toInt()}°C",
        humidity = humidity.toString(),
        feelsLike = "${feelsLike.toInt()}°C",
        weatherType = weatherType,
        weatherIconRes = weatherIconRes,
        onRefreshClick = {
            if (locationUtils.hasLocationPermission()) {
                locationUtils.requestLocationUpdate {
                    viewModel.fetchWeatherByLatLng(it.Latitude, it.Longitude)
                }
            }
        },
        onSearchClick = { showSearchDialog = true }
    )
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp
            )
        }
    }

    if (showSearchDialog) {
        SearchCityDialog(
            viewModel = viewModel,
            onDismiss = { showSearchDialog = false }
        )
    }
}

/* -------------------------------------------------- */
/* ---------------- DIALOG -------------------------- */
/* -------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val city by viewModel.newcity.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Search City") },
        text = {
            OutlinedTextField(
                value = city,
                onValueChange = { viewModel.UpdateCity(it) },
                label = { Text("Enter City") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.fetchWeatherByCityName()
                    onDismiss()
                }
            ) { Text("Search") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/* -------------------------------------------------- */
/* ---------------- SMALL COMPOSABLES --------------- */
/* -------------------------------------------------- */

@Composable
fun WeatherVerticalDivider() {
    Box(
        modifier = Modifier
            .height(45.dp)
            .width(2.dp)
            .background(Color.White.copy(alpha = 0.4f))
    )
}

@Composable
fun WeatherStatItem(
    icon: @Composable () -> Unit,
    title: String,
    value: String
) {
    Column(
        modifier = Modifier.width(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.SemiBold, maxLines = 1)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 13.sp, color = Color.White, maxLines = 1, letterSpacing = 1.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
    }
}
@Preview(
    showSystemUi = true,
    showBackground = true)
@Composable
fun WeatherScreenUIPreview() {
    WeatherScreenUI(
        city = "Delhi",
        country = "India",
        localtime = "2026-01-18 14:30",
        temp = "28°C",
        humidity = "62",
        feelsLike = "31°C",
        weatherType = "Partly Cloudy",
        weatherIconRes = R.drawable.ic_night,
        onRefreshClick = {},
        onSearchClick = {}
    )
}
