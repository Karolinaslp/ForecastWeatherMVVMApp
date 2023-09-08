package com.learning.forecastweathermmvmapp.ui.weather.current

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.forecastweathermmvmapp.data.db.entity.WeatherLocation
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.internal.lazyDeferred

private const val UNIT_SYSTEM = "UNIT_SYSTEM"
private const val DEVICE_LOCATION = "USE_DEVICE_LOCATION"
private const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider,
    private var prefs: SharedPreferences?
) : ViewModel() {
    var currentUnitSystem = MutableLiveData<String>()
    var currentCustomLocation = MutableLiveData<String>()
    var isDeviceLocation = MutableLiveData<Boolean>()

    private val prefsListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == UNIT_SYSTEM) {
                currentUnitSystem.value = sharedPreferences.getString(UNIT_SYSTEM, "def")
            }
            if (key == DEVICE_LOCATION) {
                isDeviceLocation.value = sharedPreferences.getBoolean(DEVICE_LOCATION, false)
            }
            if (key == CUSTOM_LOCATION) {
                currentCustomLocation.value = sharedPreferences.getString(CUSTOM_LOCATION, "Zgierz")
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
    suspend fun updateWeather(): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return forecastRepository.getCurrentWeather(isMetric())
    }

    suspend fun updateLocation(): LiveData<WeatherLocation> {
        return forecastRepository.getWeatherLocation()
    }

}