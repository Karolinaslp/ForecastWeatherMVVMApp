package com.learning.forecastweathermmvmapp.internal

import java.io.IOException

class NoConnectivityException: IOException()
class LocationPermissionNotGrantedException: Exception()
class DateNotFoundException: Exception()