package com.learning.forecastweathermmvmapp.ui.weather.future.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.ui.weather.current.CurrentWeatherViewModel

class FutureListWeatherViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider,
//private val prefs: SharedPreferences?

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java))
            return FutureListWeatherViewModel(forecastRepository, unitProvider) as T
        throw IllegalArgumentException("Unknown View Model")
    }
}