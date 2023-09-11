package com.learning.forecastweathermmvmapp.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.learning.forecastweathermmvmapp.data.network.response.CurrentWeatherRemoteResponse
import com.learning.forecastweathermmvmapp.data.network.response.FutureWeatherRemoteResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "6d55ac92334242dcbcf70918231109"



interface WeatherApiService {

    //http://api.weatherapi.com/v1/current.json?key=6d55ac92334242dcbcf70918231109&q=Lodz&aqi=no
    @GET("current.json")
    fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en"
    ): Deferred<CurrentWeatherRemoteResponse>

    //https://api.weatherapi.com/v1/forecast.json?key=6d55ac92334242dcbcf70918231109&q=London&days=7&aqi=no&alerts=no
    @GET("forecast.json")
    fun getFutureWeather(
        @Query("q") location: String,
        @Query("days") days: Int,
        @Query("lang") languageCode: String = "en"
    ): Deferred<FutureWeatherRemoteResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor

        ): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.weatherapi.com/v1/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}