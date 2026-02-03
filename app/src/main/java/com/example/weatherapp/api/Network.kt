package com.example.weatherapp.api


import okhttp3.Interceptor
import okhttp3.Response


import android.util.Log

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalUrl = chain.request().url
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("key", "3f14db61d8c54b11868162012261301")
            .build()

        Log.d("API_URL", newUrl.toString()) // 👈 ADD THIS

        val request = chain.request().newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(request)
    }
}
