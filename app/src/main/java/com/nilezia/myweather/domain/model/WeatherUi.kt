package com.nilezia.myweather.domain.model

data class WeatherUi(

    val mainWeather: String = "",
    val temperature: String = "0",
    val humidity: String = "",
    val windSpeed: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val feelsLike: String = "0",
    val tempMax: String = "0",
    val tempMin: String = "0",

)
