package com.krishna.openweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.krishna.openweather.adapters.TimeAndWeatherAdapter
import com.krishna.openweather.adapters.WeeklyAdapter
import com.krishna.openweather.utils.Appt
import com.krishna.openweather.databinding.ActivityMainBinding
import com.krishna.openweather.models.Forecast
import com.krishna.openweather.models.WeatherResponse
import com.krishna.openweather.utils.Constants.celsius
import com.krishna.openweather.utils.Constants.iconUrl
import com.krishna.openweather.utils.NetworkErrorDialog
import com.krishna.openweather.utils.ProgressDialog
import com.krishna.openweather.viewModels.HomeViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var userAddress: String
    private lateinit var binding: ActivityMainBinding
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        progressDialog = ProgressDialog(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

//        progressDialog.show()


    }


    private fun getAddressFromLocation() {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val city = address.locality
                val area = address.subLocality
                userAddress = "$area, $city"
                Appt.log("area = $area, $city")
                binding.address.text = userAddress
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun fetchWeather() {
        homeViewModel.getWeather(latitude, longitude)
        homeViewModel.weatherResponse.observe(this) {
            setData(it)
        }
        homeViewModel.success.observe(this){
            progressDialog.dismiss()
            if(it!=1){
                NetworkErrorDialog(this).show()
            }

        }
    }


    private fun getLocation() {
        var permissionGranted= true
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            permissionGranted = requestLocationPermission()
        }

        if(!permissionGranted) return

        progressDialog.show()

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
                    latitude = location.latitude
                    longitude = location.longitude
//                    getAddressFromLocation()
                    fetchWeather()

                }

            }


    }


    private fun requestLocationPermission(): Boolean {
        var permissionGranted= false
        val locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (!(permissions.getOrDefault(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        false
                    )) && !(permissions.getOrDefault(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        false
                    ))
                ) {
                    Appt.show(this, "Location permission not granted")

                }else{
                    permissionGranted= true
                }
            }



        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        return permissionGranted
    }


    @SuppressLint("SetTextI18n")
    private fun setData(weatherResponse: WeatherResponse) {
        val current: Forecast = weatherResponse.list.first()
        Picasso.get().load(iconUrl.format(current.weather.first().icon)).into(binding.mainIcon)
        binding.mainWeather.text = current.weather.first().main
        binding.mainWeatherDescription.text = current.weather.first().description
        binding.address.text = weatherResponse.city.name
        binding.mainTemperature.text = "${current.main.temp.roundToInt()}$celsius"
        binding.feelsLike.text = "Feels Like ${current.main.feelsLike}$celsius"
        binding.windView.text = "${current.wind.speed} m/s"
        binding.humidityView.text = current.main.humidity.toString()
        binding.pressureView.text = current.main.pressure.toString()
        binding.visibilityView.text = current.visibility.toString()
        binding.precipitationView.text = "${current.pop * 10}%}"
        binding.dewPointView.text = current.clouds.all.toString()

        setTimeAndWeather(weatherResponse.list.take(9))
        setWeekly(createWeeklyList(weatherResponse.list))


    }

    private fun setTimeAndWeather(list: List<Forecast>) {
        val recyclerView: RecyclerView = binding.timeAndTemperatureView
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = TimeAndWeatherAdapter(this, list, list.size)
    }

    private fun setWeekly(list: List<Forecast>) {
        val recyclerView: RecyclerView = binding.weeklyView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = WeeklyAdapter(this, list, list.size)
    }


    private fun createWeeklyList(forecasts: List<Forecast>): List<Forecast> {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = sdf.format(currentTime)
        val currentHour = formattedTime.substringBefore(":").toInt()

        val intervalsPassed = currentHour / 3

        val startingIndex = if (intervalsPassed < 8) {
            intervalsPassed
        } else {
            intervalsPassed % 8
        }

        val firstElementsOfDay = mutableListOf<Forecast>()
        for (i in startingIndex until forecasts.size step 8) {
            firstElementsOfDay.add(forecasts[i])
        }

        return firstElementsOfDay
    }


}