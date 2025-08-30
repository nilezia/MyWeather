package com.nilezia.myweather.domain.model

data class CurrentWeatherUi(
    val iconUrl: String = "",
    val city: String = "",
    val sunrise: String = "",
    val sunset: String = "",
    val country: String = "",
    val weatherUi: WeatherUi = WeatherUi()
)
