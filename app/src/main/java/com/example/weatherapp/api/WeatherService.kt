package com.example.weatherapp.api

import com.example.weatherapp.data.WeatherResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(ApiKeyInterceptor())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.weatherapi.com/v1/")
    .client(okHttpClient)   // ✅ VERY IMPORTANT
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val weatherservice: WeatherService =
    retrofit.create(WeatherService::class.java)

interface WeatherService {

    @GET("forecast.json")
    suspend fun getForecast(

        @Query("q") city: String,
        @Query("days") days: Int
    ): WeatherResponse
}
