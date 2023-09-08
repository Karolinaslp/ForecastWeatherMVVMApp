package com.learning.forecastweathermmvmapp.data.network.response

import com.google.gson.annotations.SerializedName
import com.learning.forecastweathermmvmapp.data.db.entity.FutureWeatherEntry


data class ForecastDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)