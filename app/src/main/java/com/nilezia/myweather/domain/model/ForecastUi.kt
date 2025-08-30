package com.nilezia.myweather.domain.model

import com.nilezia.myweather.data.model.ForcastResponse

data class ForecastUi(
    val list: List<WeatherUi?>? = null,
    val city: ForcastResponse.City? = null,

)


