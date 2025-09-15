package com.nilezia.myweather.data.api

import com.nilezia.myweather.BuildConfig
import com.nilezia.myweather.data.model.DirectLocationResponse
import com.nilezia.myweather.data.model.ForcastResponse
import com.nilezia.myweather.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather")
    suspend fun getDailyWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = BuildConfig.OPEN_WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

    @GET("data/2.5/forecast")
    suspend fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = BuildConfig.OPEN_WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ): Response<ForcastResponse>

    @GET("/geo/1.0/direct")
    suspend fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") appId: String = BuildConfig.OPEN_WEATHER_API_KEY,
    ): Response<List<DirectLocationResponse>>
}