package com.learning.forecastweathermmvmapp.ui.weather.current

import androidx.lifecycle.ViewModelProvider
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository


class CurrentWeatherViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider

) : ViewModelProvider.NewInstanceFactory() {

//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return CurrentWeatherViewModel(forecastRepository, unitProvider) as T
//    }
}