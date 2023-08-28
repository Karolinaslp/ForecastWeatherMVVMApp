package com.learning.forecastweathermmvmapp.data.network.response

import com.google.gson.annotations.SerializedName
import com.learning.forecastweathermmvmapp.data.db.entity.WeatherLocation


data class CurrentWeatherRemoteResponse(
    val location: WeatherLocation,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherRemoteEntry
)