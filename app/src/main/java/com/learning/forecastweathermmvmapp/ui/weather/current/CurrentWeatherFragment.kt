package com.learning.forecastweathermmvmapp.ui.weather.current

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.learning.forecastweathermmvmapp.data.network.ConnectivityInterceptorImpl
import com.learning.forecastweathermmvmapp.data.network.WeatherApiService
import com.learning.forecastweathermmvmapp.data.network.WeatherNetworkDataSourceImpl
import com.learning.forecastweathermmvmapp.databinding.FragmentCurrentWeatherBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrentWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentWeatherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentWeatherViewModel::class.java)

        val apiService = WeatherApiService(ConnectivityInterceptorImpl(this.context!!))
        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)
        weatherNetworkDataSource.downloadedCurrentWeather.observe(this, Observer {
            binding.textView.text = it.toString()
        })

        GlobalScope.launch(Dispatchers.Main) {
           weatherNetworkDataSource.fetchCurrentWeather("Zgierz", "pl")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}