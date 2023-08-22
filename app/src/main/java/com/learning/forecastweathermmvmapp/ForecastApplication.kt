package com.learning.forecastweathermmvmapp

import android.app.Application
import androidx.room.Room
import com.jakewharton.threetenabp.AndroidThreeTen
import com.learning.forecastweathermmvmapp.data.db.ForecastDatabase
import com.learning.forecastweathermmvmapp.data.network.ConnectivityInterceptor
import com.learning.forecastweathermmvmapp.data.network.ConnectivityInterceptorImpl
import com.learning.forecastweathermmvmapp.data.network.WeatherApiService
import com.learning.forecastweathermmvmapp.data.network.WeatherNetworkDataSource
import com.learning.forecastweathermmvmapp.data.network.WeatherNetworkDataSourceImpl
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepositoryImpl
import com.learning.forecastweathermmvmapp.ui.weather.current.CurrentWeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ForecastApplication : Application() {
//    private val db by inject<ForecastDatabase>()
//    private val dao by inject<CurrentWeatherDao>()


    //Create Koin module
    private val appModule = module {
        single { Room.databaseBuilder(get(),ForecastDatabase::class.java, "forecast.db").build() }
        single { get<ForecastDatabase>().currentWeatherDao() }
        single<ConnectivityInterceptor> { ConnectivityInterceptorImpl(applicationContext) }
        single { WeatherApiService(get()) }
        single<WeatherNetworkDataSource> { WeatherNetworkDataSourceImpl(get()) }
        single<ForecastRepository> { ForecastRepositoryImpl(get(), get()) }
        viewModel { CurrentWeatherViewModel(get()) }

    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        //Create and register KoinApplication instance
        startKoin {
            //androidLogger()
            androidContext(this@ForecastApplication)
            modules(appModule)
        }
    }
}