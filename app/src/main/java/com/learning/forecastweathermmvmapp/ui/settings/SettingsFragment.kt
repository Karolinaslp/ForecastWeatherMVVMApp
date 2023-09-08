package com.learning.forecastweathermmvmapp.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.learning.forecastweathermmvmapp.R
import com.learning.forecastweathermmvmapp.ui.weather.current.CurrentWeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment() : PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
       addPreferencesFromResource(R.xml.preferences)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }
}