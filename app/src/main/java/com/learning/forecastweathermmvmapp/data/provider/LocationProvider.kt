package com.learning.forecastweathermmvmapp.data.provider

import com.learning.forecastweathermmvmapp.data.db.entity.WeatherLocation

interface LocationProvider {
   suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation) : Boolean
   suspend fun getPreferredLocationString(): String
}