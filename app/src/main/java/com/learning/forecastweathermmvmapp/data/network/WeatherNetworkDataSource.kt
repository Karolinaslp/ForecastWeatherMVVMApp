package com.learning.forecastweathermmvmapp.data.network

import androidx.lifecycle.LiveData
import com.learning.forecastweathermmvmapp.data.network.response.CurrentWeatherRemoteResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherRemoteResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}