package com.learning.forecastweathermmvmapp.ui.weather.current

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository


@Suppress("UNCHECKED_CAST")
class CurrentWeatherViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider,
    private val prefs: SharedPreferences?

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java))
            return CurrentWeatherViewModel(forecastRepository,unitProvider,prefs) as T
        throw IllegalArgumentException("Unknown View Model")
    }
}