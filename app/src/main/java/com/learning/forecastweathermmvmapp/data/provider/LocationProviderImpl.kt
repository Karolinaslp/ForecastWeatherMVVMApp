package com.learning.forecastweathermmvmapp.data.provider

import com.learning.forecastweathermmvmapp.data.db.entity.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        return true
    }

    override suspend fun getPreferredLocationString(): String {
        return "Lublin"
    }
}