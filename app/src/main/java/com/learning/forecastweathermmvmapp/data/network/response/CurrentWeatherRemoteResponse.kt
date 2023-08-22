package com.learning.forecastweathermmvmapp.data.network.response

import com.google.gson.annotations.SerializedName
import com.learning.forecastweathermmvmapp.data.db.entity.Location


data class CurrentWeatherRemoteResponse(
    val location: Location,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherRemoteEntry
)