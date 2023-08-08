package com.learning.forecastweathermmvmapp.data.network

import androidx.lifecycle.LiveData
import com.learning.forecastweathermmvmapp.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}