package com.learning.forecastweathermmvmapp

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.jakewharton.threetenabp.AndroidThreeTen
import com.learning.forecastweathermmvmapp.data.db.ForecastDatabase
import com.learning.forecastweathermmvmapp.data.network.ConnectivityInterceptor
import com.learning.forecastweathermmvmapp.data.network.ConnectivityInterceptorImpl
import com.learning.forecastweathermmvmapp.data.network.WeatherApiService
import com.learning.forecastweathermmvmapp.data.network.WeatherNetworkDataSource
import com.learning.forecastweathermmvmapp.data.network.WeatherNetworkDataSourceImpl
import com.learning.forecastweathermmvmapp.data.provider.UnitProvider
import com.learning.forecastweathermmvmapp.data.provider.UnitProviderImpl
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepository
import com.learning.forecastweathermmvmapp.data.repository.ForecastRepositoryImpl
import com.learning.forecastweathermmvmapp.ui.weather.current.CurrentWeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ForecastApplication : Application() {

    //Create Koin module
    private val appModule = module {
        single { Room.databaseBuilder(get(), ForecastDatabase::class.java, "forecast.db").build() }
        single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
        single { get<ForecastDatabase>().currentWeatherDao() }
        single<ConnectivityInterceptor> { ConnectivityInterceptorImpl(get()) }
        single { WeatherApiService(get()) }
        single<WeatherNetworkDataSource> { WeatherNetworkDataSourceImpl(get()) }
        factory<ForecastRepository> { ForecastRepositoryImpl(get(), get()) }
        factory<UnitProvider> { UnitProviderImpl(get()) }
        viewModel { CurrentWeatherViewModel(get(), get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        //Create and register KoinApplication instance
        startKoin {
            //androidLogger()
            androidContext(this@ForecastApplication)
            modules(appModule)
        }
    }
}