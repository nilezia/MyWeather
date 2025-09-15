package com.nilezia.myweather.domain.model

data class DirectLocationUi(
    val name: String = "",
    val localNames: String = "",
    val lat: Double = -1.0,
    val lon: Double = -1.0,
    val country: String = "",
    val state: String = ""
)
