package com.learning.forecastweathermmvmapp.ui.weather.current

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider,
    var prefs: SharedPreferences?
) : ViewModel() {
    var currentTemperature= MutableLiveData<Double>()
    private val unitSystem = unitProvider.getUnitSystem()
    fun isMetric(): Boolean{
        return prefs?.getString("UNIT_SYSTEM", "METRIC").equals("METRIC")
    }

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric())
    }
    fun getData(): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return forecastRepository.getWeatherTest(isMetric())
    }
}