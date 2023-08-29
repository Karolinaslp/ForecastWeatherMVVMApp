package com.learning.forecastweathermmvmapp.ui.weather.current

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.internal.lazyDeferred

private const val UNIT_SYSTEM = "UNIT_SYSTEM"

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider,
    private var prefs: SharedPreferences?
) : ViewModel() {
    var currentUnitSystem = MutableLiveData<String>()

    private val prefsListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == UNIT_SYSTEM) {
                currentUnitSystem.value = sharedPreferences.getString(UNIT_SYSTEM, "def")
            }
        }

    init {
        prefs?.registerOnSharedPreferenceChangeListener(prefsListener)
    }

    fun isMetric(): Boolean {
        return prefs?.getString("UNIT_SYSTEM", "METRIC").equals("METRIC")
    }

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric())
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }

    fun getWeatherFromDatabase(): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return forecastRepository.getWeatherFromDatabase(isMetric())
    }
}