package com.learning.forecastweathermmvmapp.data.repository

import androidx.lifecycle.LiveData
import com.learning.forecastweathermmvmapp.data.db.entity.WeatherLocation
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    fun getWeatherFromDatabase(isMetric:Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
  //  fun getWeatherLocationFromDatabase(): LiveData<WeatherLocation>
}