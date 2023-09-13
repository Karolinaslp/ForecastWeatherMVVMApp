package com.learning.forecastweathermmvmapp.ui.weather.future.list

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository

class FutureListWeatherViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider,
    private val prefs: SharedPreferences?

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FutureListWeatherViewModel::class.java))
            return FutureListWeatherViewModel(forecastRepository, unitProvider, prefs) as T
        throw IllegalArgumentException("Unknown View Model")
    }
}