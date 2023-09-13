package com.learning.forecastweathermmvmapp.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.learning.forecastweathermmvmapp.data.db.entity.WeatherLocation
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.internal.UnitSystem
import com.learning.forecastweathermmvmapp.internal.lazyDeferred

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit: Boolean
        get() = unitSystem == UnitSystem.METRIC

    open val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }

//    suspend fun updateWeather(date: LocalDate, isMetric: Boolean): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
//        return forecastRepository.getFutureWeatherList(date, isMetric)
//    }

    suspend fun updateLocation(): LiveData<WeatherLocation> {
        return forecastRepository.getWeatherLocation()
    }

}