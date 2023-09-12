package com.learning.forecastweathermmvmapp.ui.weather.future.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.learning.forecastweathermmvmapp.data.db.LocalDateConverter
import com.learning.forecastweathermmvmapp.databinding.FragmentFutureDetailWeatherBinding
import com.learning.forecastweathermmvmapp.internal.DateNotFoundException
import com.learning.forecastweathermmvmapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.item_future_weather.imageView_condition_icon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureDetailWeatherFragment : ScopedFragment() {

    private val viewModelFactoryInstanceFactory: ((LocalDate) -> FutureDetailWeatherViewModelFactory) by inject()

    private lateinit var viewModel: FutureDetailWeatherViewModel

    private var _binding: FragmentFutureDetailWeatherBinding? = null
    private val binding
        get() = _binding!!


    companion object {
        fun newInstance() = FutureDetailWeatherFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFutureDetailWeatherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs = arguments?.let { FutureDetailWeatherFragmentArgs.fromBundle(it) }
        val date =  LocalDateConverter.stringToDate(safeArgs?.dateString) ?: throw DateNotFoundException()

        viewModel = ViewModelProvider(this, viewModelFactoryInstanceFactory(date)).get(FutureDetailWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@FutureDetailWeatherFragment, Observer { location ->
            if(location == null) return@Observer
            updateLocation(location.name)
        })

        futureWeather.observe(this@FutureDetailWeatherFragment, Observer { weatherEntry ->
            updateDate(weatherEntry.date)
            updateTemperatures(weatherEntry.avgTemperature, weatherEntry.minTemperature, weatherEntry.maxTemperature)
            updateCondition(weatherEntry.conditionText)
            updatePrecipitation(weatherEntry.totalPrecipitation)
            updateWindSpeed(weatherEntry.maxWind)
            updateVisibility(weatherEntry.avgVisibilityDistance)
            updateUv(weatherEntry.uv)

            Glide.with(this@FutureDetailWeatherFragment)
                .load("https:" + weatherEntry.conditionIconUrl)
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetricUnit) metric else imperial
    }

    private fun updateUv(uv: Double) {
        binding.textViewUv.text = "UVL $uv"
    }

    private fun updateVisibility(avgVisibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        binding.textViewVisibility.text = "Visibility: $avgVisibilityDistance $unitAbbreviation"
    }

    private fun updateWindSpeed(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        binding.textViewWind.text = "Wind speed: $windSpeed $unitAbbreviation"
    }

    private fun updatePrecipitation(totalPrecipitation: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        binding.textViewPrecipitation.text = "Precipitation: $totalPrecipitation $unitAbbreviation"
    }

    private fun updateCondition(conditionText: String) {
        binding.textViewCondition.text = conditionText
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateTemperatures(
        temperature: Double,
        minTemperature: Double,
        maxTemperature: Double
    ) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        binding.textViewTemperature.text = "$temperature$unitAbbreviation"
        binding.textViewMinMaxTemperature.text = "Min: $minTemperature$unitAbbreviation, Max: $maxTemperature$unitAbbreviation"
    }

    private fun updateDate(date: LocalDate) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}