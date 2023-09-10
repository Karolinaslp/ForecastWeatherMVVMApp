package com.learning.forecastweathermmvmapp

import android.app.Application
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.learning.forecastweathermmvmapp.data.db.ForecastDatabase
import com.learning.forecastweathermmvmapp.data.network.ConnectivityInterceptor
import com.learning.forecastweathermmvmapp.data.network.ConnectivityInterceptorImpl
import com.learning.forecastweathermmvmapp.data.network.WeatherApiService
import com.learning.forecastweathermmvmapp.data.network.WeatherNetworkDataSource
import com.learning.forecastweathermmvmapp.data.network.WeatherNetworkDataSourceImpl
import com.learning.forecastweathermmvmapp.data.provider.LocationProvider
import com.learning.forecastweathermmvmapp.data.provider.LocationProviderImpl
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.provider.UnitProviderImpl
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepositoryImpl
import com.learning.forecastweathermmvmapp.ui.weather.current.CurrentWeatherViewModelFactory
import com.learning.forecastweathermmvmapp.ui.weather.future.list.FutureListWeatherViewModelFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ForecastApplication : Application() {

    //Create Koin module
    private val appModule = module {
//        single { Room.databaseBuilder(get(), ForecastDatabase::class.java, "forecast.db").build() }
        single { ForecastDatabase.buildDatabase(get()) }
        single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
        single { get<ForecastDatabase>().currentWeatherDao() }
        single { get<ForecastDatabase>().futureWeatherDao() }
        single { get<ForecastDatabase>().weatherLocationDao() }
        single<ConnectivityInterceptor> { ConnectivityInterceptorImpl(get()) }
        single { WeatherApiService(get()) }
        single<WeatherNetworkDataSource> { WeatherNetworkDataSourceImpl(get()) }
        single<LocationProvider> { LocationProviderImpl(get(), get()) }
        single { LocationServices.getFusedLocationProviderClient(androidContext()) }
        factory<ForecastRepository> { ForecastRepositoryImpl(get(), get(), get(), get(), get()) }
        factory<UnitProvider> { UnitProviderImpl(get()) }
        factory { CurrentWeatherViewModelFactory(get(), get(), get()) }
        factory { FutureListWeatherViewModelFactory(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        //Create and register KoinApplication instance
        startKoin {
            androidLogger()
            androidContext(this@ForecastApplication)
            modules(appModule)
        }
    }
}
