package com.krishna.openweather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.krishna.openweather.Appt
import com.krishna.openweather.databinding.ActivityMainBinding
import com.krishna.openweather.models.WeatherResponse
import com.krishna.openweather.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var userAddress: String
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if(!getLocation()) return

        homeViewModel.getWeather(latitude, longitude)
        homeViewModel.weatherResponse.observe(this) {
            setData(it)
        }
    }

    private fun setData(weatherResponse: WeatherResponse){


    }


    private fun getAddressFromLocation(latitude: Double, longitude: Double): Boolean {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            return if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val city = address.locality
                val area = address.subLocality
                userAddress = "$area, $city"
                Appt.log("area = $area, $city")

                true
            } else {
                false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }


    private fun getLocation(): Boolean {
        var permissionGranted = false
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        permissionGranted = true
                    }

                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {

                        permissionGranted = true
                    }

                    else -> {
                        Appt.show(this, "Location permission not granted")
                        permissionGranted = false
                    }
                }
            }



            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

        }

        if (permissionGranted) {
            var gotAddress = false
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                    else {
                        val lat = location.latitude
                        val lon = location.longitude
                        gotAddress = getAddressFromLocation(lat, lon)
                        Appt.show(this, "lat: $lat lon: $lon")
                    }

                }

            return gotAddress
        } else {
            return false
        }

    }


}