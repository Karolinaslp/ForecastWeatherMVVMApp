package com.learning.forecastweathermmvmapp.ui.weather.future.detail

import androidx.lifecycle.LiveData
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.internal.lazyDeferred
import com.learning.forecastweathermmvmapp.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureDetailWeatherViewModel(
    private val detailDate: LocalDate,
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeferred {
        forecastRepository.getFutureWeatherByDate(detailDate, super.isMetricUnit)
    }

     suspend fun updateWeather(): LiveData<out UnitSpecificDetailFutureWeatherEntry>{
        return forecastRepository.getFutureWeatherByDate(detailDate, super.isMetricUnit)
    }
}