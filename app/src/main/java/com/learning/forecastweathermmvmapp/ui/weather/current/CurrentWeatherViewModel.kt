package com.learning.forecastweathermmvmapp.ui.weather.current

import androidx.lifecycle.ViewModel
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.internal.UnitSystem
import com.learning.forecastweathermmvmapp.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC//get from settings later
     val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}