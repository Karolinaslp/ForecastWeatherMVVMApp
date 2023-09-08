package com.learning.forecastweathermmvmapp.data.network.response


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.learning.forecastweathermmvmapp.data.db.entity.Day

@Entity(tableName = "future_weather", indices = [Index(value =["date"], unique = true)])
data class FutureWeatherRemoteEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val date: String,
    @Embedded
    val day: Day,
)