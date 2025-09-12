package com.nilezia.myweather.domain.model

data class DailyWeatherUi(
    val city: String = "",
    val sunrise: String = "",
    val sunset: String = "",
    val country: String = "",
    val weatherUi: WeatherUi = WeatherUi()
)
