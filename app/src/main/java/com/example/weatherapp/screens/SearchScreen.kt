package com.example.weatherapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreenUI(city: String,
                    country: String,
                    localtime : String,
                    temp: String,
                    humidity: String,
                    feelsLike: String,
                    weatherType: String,
                    weatherIconRes: Int,
                    icontype : String,
                    onRefreshClick: () -> Unit

) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = weatherIconRes),
            contentDescription = "Weather Icon",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize()){
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 40.dp, start = 15.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Icon(Icons.Default.LocationOn,contentDescription = null, tint = colorResource(R.color.white))
                Spacer(modifier = Modifier.width(8.dp))

                Text(text = city, fontSize = 30.sp, fontWeight = FontWeight.Bold
                    , color = colorResource(R.color.white))
                IconButton(onClick = onRefreshClick) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onRefreshClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Refresh",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

            }
            Text(text = "$country • $localtime", fontSize = 20.sp, fontWeight = FontWeight.Medium
                , color = colorResource(R.color.white), textAlign = TextAlign.Center, modifier = Modifier.padding(top = 0.dp, start = 15.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center){

                Text(text = temp, fontSize = 80.sp, fontWeight = FontWeight.Bold
                    , color = colorResource(R.color.white), textAlign = TextAlign.Center, modifier = Modifier.padding(top = 110.dp, start = 110.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( vertical = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                WeatherStatItem(
                    icon = {
                        AsyncImage(
                            model = icontype,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    title = "WEATHER",
                    value = weatherType
                )

                WeatherVerticalDivider()

                WeatherStatItem(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.outline_weather_snowy_24),
                            contentDescription = null,
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
                            painter = painterResource(R.drawable.outline_weather_snowy_24),
                            contentDescription = null,
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

