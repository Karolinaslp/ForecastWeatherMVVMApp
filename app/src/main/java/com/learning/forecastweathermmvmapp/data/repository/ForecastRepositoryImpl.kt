package com.learning.forecastweathermmvmapp.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import com.learning.forecastweathermmvmapp.data.db.CurrentWeatherDao
import com.learning.forecastweathermmvmapp.data.db.FutureWeatherDao
import com.learning.forecastweathermmvmapp.data.db.WeatherLocationDao
import com.learning.forecastweathermmvmapp.data.db.entity.WeatherLocation
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import com.learning.forecastweathermmvmapp.data.network.FORECAST_DAYS_COUNT
import com.learning.forecastweathermmvmapp.data.network.WeatherNetworkDataSource
import com.learning.forecastweathermmvmapp.data.network.response.CurrentWeatherRemoteResponse
import com.learning.forecastweathermmvmapp.data.network.response.FutureWeatherRemoteResponse
import com.learning.forecastweathermmvmapp.data.provider.LocationProvider
import com.learning.forecastweathermmvmapp.internal.mapToLocalEntry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.Locale

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
            downloadedFutureWeather.observeForever { newFutureWeather ->
                persistFetchedFutureWeather(newFutureWeather)
            }
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) futureWeatherDao.getSimpleWeatherForecastsMetric(
                startDate
            )
            else futureWeatherDao.getSimpleWeatherForecastsImperial(startDate)
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherRemoteResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry.mapToLocalEntry())
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherRemoteResponse) {
        fun deleteOldForecastData() {
            val today = LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList = fetchedWeather.forecastWeatherEntries.entries
            futureWeatherDao.insert(futureWeatherList)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()

        if (lastWeatherLocation == null
            || locationProvider.hasLocationChanged(lastWeatherLocation)
        ) {
            Log.d("before weather data", "Fetch Before")
            fetchCurrentWeather()
            fetchFutureWeather()
            Log.d("after fetching", "Fetched")
            return
        }
        //if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
        fetchCurrentWeather()

        //if(isFetchFutureNeeded())
        fetchFutureWeather()
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    private suspend fun fetchFutureWeather() {
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchFutureNeeded() : Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }

    override fun getWeatherFromDatabase(isMetric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return if (isMetric) currentWeatherDao.getWeatherMetric()
        else currentWeatherDao.getWeatherImperial()
    }
}


