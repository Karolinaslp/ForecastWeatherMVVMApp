package com.learning.forecastweathermmvmapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learning.forecastweathermmvmapp.data.network.response.CurrentWeatherRemoteResponse
import com.learning.forecastweathermmvmapp.data.network.response.FutureWeatherRemoteResponse
import com.learning.forecastweathermmvmapp.internal.NoConnectivityException

const val FORECAST_DAYS_COUNT = 7

class WeatherNetworkDataSourceImpl(
    private val weatherApiService: WeatherApiService,
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherRemoteResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherRemoteResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather = weatherApiService
                .getCurrentWeather(location, languageCode)
                .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherRemoteResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherRemoteResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchFutureWeather(
        location: String,
        languageCode: String
    ) {
        try {
            val fetchedFutureWeather = weatherApiService
                .getFutureWeather(location, FORECAST_DAYS_COUNT, languageCode)
                .await()
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}