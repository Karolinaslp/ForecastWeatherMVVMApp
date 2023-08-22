package com.learning.forecastweathermmvmapp.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import java.lang.IllegalArgumentException


class CurrentWeatherViewModelFactory(private val forecastRepository: ForecastRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java)) {
            return CurrentWeatherViewModel(forecastRepository) as T
        }
        throw IllegalArgumentException("unknown ViewModel class")
    }

}