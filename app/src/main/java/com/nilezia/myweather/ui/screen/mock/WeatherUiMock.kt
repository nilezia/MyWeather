package com.nilezia.myweather.ui.screen.mock

import com.nilezia.myweather.domain.model.DailyWeatherUi
import com.nilezia.myweather.domain.model.WeatherUi

fun weatherUiMock() = DailyWeatherUi(
    city = "หนองจอก",
    sunset = "17:00",
    sunrise = "06:00",
    country = "TH",
    weatherUi = WeatherUi(
        windSpeed = "1.5",
        humidity = "80",
        description = "ฝนตกหนัก",
        iconUrl = "https://openweathermap.org/img/wn/10d@2x.png",
        feelsLike = "25",
        tempMax = "30",
        tempMin = "18",
        temperature = "99"

    )
)