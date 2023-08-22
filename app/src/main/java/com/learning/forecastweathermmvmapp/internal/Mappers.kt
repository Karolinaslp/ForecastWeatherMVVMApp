package com.learning.forecastweathermmvmapp.internal

import com.learning.forecastweathermmvmapp.data.db.entity.CurrentWeatherEntry
import com.learning.forecastweathermmvmapp.data.network.response.CurrentWeatherRemoteEntry

fun CurrentWeatherRemoteEntry.mapToLocalEntry() = CurrentWeatherEntry(
    this.feelslikeC,
    this.isDay,
    this.condition,
    this.feelslikeF,
    this.precipIn,
    this.precipMm,
    this.tempC,
    this.tempF,
    this.visKm,
    this.visMiles,
    this.windDir,
    this.windKph,
    this.windMph
)
