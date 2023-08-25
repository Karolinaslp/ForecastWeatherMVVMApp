package com.learning.forecastweathermmvmapp.ui.weather.current

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.internal.UnitSystem
import com.learning.forecastweathermmvmapp.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider,
    var prefs: SharedPreferences?
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()
    var currentTemperature = MutableLiveData<Double>()
    fun isMetric(): Boolean {
//        get() = unitSystem == UnitSystem.METRIC
        return prefs?.getString("UNIT_SYSTEM", "looool").equals("METRIC")
    }

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric())
    }
}