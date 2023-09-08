package com.learning.forecastweathermmvmapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learning.forecastweathermmvmapp.data.db.entity.FutureWeatherEntry
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.ImperialSpecificSimpleFutureWeatherEntry
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.MetricSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherEntry: FutureWeatherEntry)

    @Query("SELECT  * FROM future_weather WHERE date(date) >= date(:startDate)")
    fun getSimpleWeatherForecastsMetric(startDate: LocalDate):LiveData<List<MetricSpecificSimpleFutureWeatherEntry>>

    @Query("SELECT * FROM future_weather WHERE date(date) >= date(:startDate)")
    fun getSimpleWeatherForecastsImperial(startDate: LocalDate): LiveData<List<ImperialSpecificSimpleFutureWeatherEntry>>

    @Query("SELECT COUNT(id) FROM future_weather WHERE date(date) >= date(:startDate)")
    fun countFutureWeather(startDate: LocalDate): Int

    @Query("DELETE FROM future_weather WHERE date(date) < date(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDate)
}