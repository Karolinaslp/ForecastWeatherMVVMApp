package com.learning.forecastweathermmvmapp.data.network

import androidx.lifecycle.LiveData
import com.learning.forecastweathermmvmapp.data.network.response.CurrentWeatherRemoteResponse
import com.learning.forecastweathermmvmapp.data.network.response.FutureWeatherRemoteResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherRemoteResponse>
    val downloadedFutureWeather: LiveData<FutureWeatherRemoteResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )

    suspend fun fetchFutureWeather(
        location: String,
        languageCode: String
    )
}