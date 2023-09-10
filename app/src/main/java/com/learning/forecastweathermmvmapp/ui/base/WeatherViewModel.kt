package com.learning.forecastweathermmvmapp.ui.base

import androidx.lifecycle.ViewModel
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
}