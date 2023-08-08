package com.learning.forecastweathermmvmapp.data.network.response

import com.google.gson.annotations.SerializedName
import com.learning.forecastweathermmvmapp.data.db.entity.CurrentWeatherEntry
import com.learning.forecastweathermmvmapp.data.db.entity.Location


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location
)