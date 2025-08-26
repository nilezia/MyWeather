package com.nilezia.myweather.data.repository

import android.location.Location

interface WeatherRepository {
    fun getWeather(location: Location)
}

class WeatherRepositoryImpl: WeatherRepository {
    override fun getWeather(location: Location) {

    }

}