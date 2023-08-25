package com.learning.forecastweathermmvmapp.data.provider

import com.learning.forecastweathermmvmapp.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}