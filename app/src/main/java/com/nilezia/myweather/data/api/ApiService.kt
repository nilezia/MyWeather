package com.nilezia.myweather.data.api

import com.nilezia.myweather.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    suspend fun getCurrentWeather(@Query("lat") lat: Double,
                                  @Query("lon") lon: Double,
                                  @Query("appid") appId: String = BuildConfig.OPEN_WEATHER_API_KEY,
                                  @Query("units") units: String = "metric"): Response<WeatherResponse>

}