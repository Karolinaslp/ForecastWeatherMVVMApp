package com.learning.forecastweathermmvmapp.data.network.response


import com.google.gson.annotations.SerializedName
import com.learning.forecastweathermmvmapp.data.db.entity.WeatherLocation

data class FutureWeatherRemoteResponse(
    @SerializedName("forecast")
    val forecastWeatherEntries: ForecastDaysContainer,
    val location: WeatherLocation
)