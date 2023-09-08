package com.learning.forecastweathermmvmapp.ui.weather.current

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.learning.forecastweathermmvmapp.databinding.FragmentCurrentWeatherBinding
import com.learning.forecastweathermmvmapp.ui.base.ScopedFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class CurrentWeatherFragment() : ScopedFragment() {
    private val viewModelFactory: CurrentWeatherViewModelFactory by inject()
    private lateinit var  viewModel: CurrentWeatherViewModel

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding
        get() = _binding!!

    private val UNIT_SYSTEM_KEY = "UNIT_SYSTEM"
    private lateinit var prefs: SharedPreferences

    private val listener = object : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            Log.d("TAG", "return value BATMAN")
            if (key == UNIT_SYSTEM_KEY) {
                val temp = sharedPreferences?.getString(UNIT_SYSTEM_KEY, "mmm")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory)[CurrentWeatherViewModel::class.java]
        _binding = FragmentCurrentWeatherBinding.inflate(layoutInflater, container, false)

        viewModel.currentUnitSystem.observe(viewLifecycleOwner) {
            bindUI()
        }
        viewModel.currentCustomLocation.observe(viewLifecycleOwner) {
            bindUI()
        }
        viewModel.isDeviceLocation.observe(viewLifecycleOwner) {
            bindUI()
        }
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()

        prefs =
            context?.applicationContext?.let { PreferenceManager.getDefaultSharedPreferences(it) }!!
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.updateWeather()
        val weatherLocation = viewModel.updateLocation()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if (location == null) return@Observer

            updateLocation(location.name)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer

            binding.groupLoading.visibility = View.GONE

            updateDateToToday()
            updateTemperature(it.temperature, it.feelsLikeTemperature)
            updateCondition(it.conditionText)
            updatePrecipitation(it.precipitationVolume)
            updateWind(it.windDirection, it.windSpeed)
            updateVisibility(it.visibilityDistance)

            Glide.with(this@CurrentWeatherFragment)
                .load("https:${it.conditionIconUrl}")
                .into(binding.imageViewConditionIcon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric()) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperature(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        binding.textViewTemperature.text = "$temperature$unitAbbreviation"
        binding.textViewFeelsLikeTemperature.text = "Feels like $feelsLike$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        binding.textViewCondition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        binding.textViewPrecipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWind(windDirectiion: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        binding.textViewWind.text = "Wind: $windDirectiion, $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        binding.textViewVisibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}