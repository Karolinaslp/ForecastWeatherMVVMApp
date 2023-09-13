package com.learning.forecastweathermmvmapp.ui.weather.future.list

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.internal.lazyDeferred
import com.learning.forecastweathermmvmapp.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate


private const val UNIT_SYSTEM = "UNIT_SYSTEM"

class FutureListWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider,
    private var prefs: SharedPreferences?
) : WeatherViewModel(forecastRepository, unitProvider) {
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

    val weatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now(), super.isMetricUnit)
    }

    suspend fun updateWeatherEntries(): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return forecastRepository.getFutureWeatherList(LocalDate.now(), isMetric())
    }
}