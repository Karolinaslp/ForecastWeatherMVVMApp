package com.learning.forecastweathermmvmapp.data.repository

import androidx.lifecycle.LiveData
import com.learning.forecastweathermmvmapp.data.db.entity.WeatherLocation
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    fun getWeatherFromDatabase(isMetric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>

    suspend fun getFutureWeatherList(startDate: LocalDate, metric: Boolean ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>
}