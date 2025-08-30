package com.nilezia.myweather.data.model

data class ForcastResponse(
    val city: City? = null,
    val cnt: Int? = null,
    val cod: String? = null,
    val list: List<Item0?>? = null,
    val message: Int? = null
) {
    data class City(
        val coord: Coord? = null,
        val country: String? = null,
        val id: Int? = null,
        val name: String? = null,
        val population: Int? = null,
        val sunrise: Int? = null,
        val sunset: Int? = null,
        val timezone: Int? = null
    ) {
        data class Coord(
            val lat: Double? = null,
            val lon: Double? = null
        )
    }

    data class Item0(
        val clouds: WeatherResponse.Clouds? = null,
        val dt: Int? = null,
        val dt_txt: String? = null,
        val main: WeatherResponse.Main? = null,
        val pop: Double? = null,
        val rain: WeatherResponse.Rain? = null,
        val sys: WeatherResponse.Sys? = null,
        val visibility: Int? = null,
        val weather: List<Weather?>? = null,
        val wind: WeatherResponse.Wind? = null
    ) {

        data class Weather(
            val description: String? = null,
            val icon: String? = null,
            val id: Int? = null,
            val main: String? = null
        )


    }
}