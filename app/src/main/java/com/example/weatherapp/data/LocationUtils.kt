package com.example.weatherapp.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class LocationUtils(private val context: Context) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun requestLocationUpdate(
        onLocationReceived: (LocationData) -> Unit
    ) {

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000L // 2 seconds
        )
            .setMinUpdateDistanceMeters(10f)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {

                val loc = result.lastLocation ?: return

                // 🌍 Geocoder background thread
                CoroutineScope(Dispatchers.IO).launch {

                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(
                        loc.latitude,
                        loc.longitude,
                        1
                    )

                    val address = addresses?.firstOrNull()

                    val city = when {
                        !address?.locality.isNullOrBlank() ->
                            address.locality

                        !address?.subAdminArea.isNullOrBlank() ->
                            address.subAdminArea

                        !address?.adminArea.isNullOrBlank() ->
                            address.adminArea

                        else -> "Delhi"
                    }


                    withContext(Dispatchers.Main) {
                        onLocationReceived(
                            LocationData(
                                Latitude = loc.latitude,
                                Longitude = loc.longitude,
                                city = city
                            )
                        )
                    }
                }

                // 🔴 IMPORTANT: stop updates after first valid result
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
}
