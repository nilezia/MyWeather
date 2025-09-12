package com.nilezia.myweather.domain

import com.nilezia.myweather.data.model.WeatherResponse
import com.nilezia.myweather.data.repository.WeatherRepository
import com.nilezia.myweather.domain.model.DailyWeatherUi
import com.nilezia.myweather.domain.model.WeatherUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

interface GetWeatherUseCase {
    fun execute(lat: Double, lon: Double): Flow<DailyWeatherUi>
}

class GetWeatherUseCaseImpl @Inject constructor(private val weatherRepository: WeatherRepository) :
    GetWeatherUseCase {
    override fun execute(lat: Double, lon: Double): Flow<DailyWeatherUi> {

        return weatherRepository.getDailyWeather(lat, lon).map {
            mapDataToDomain(it)
        }
    }

    private fun mapDataToDomain(weatherResponse: WeatherResponse): DailyWeatherUi {
        return DailyWeatherUi(
            weatherUi = WeatherUi(
                mainWeather = weatherResponse.weather?.firstOrNull()?.main.orEmpty(),
                temperature = weatherResponse.main?.temp.toString(),
                humidity = weatherResponse.main?.humidity.toString(),
                windSpeed = weatherResponse.wind?.speed.toString(),
                description = weatherResponse.weather?.firstOrNull()?.description.orEmpty(),
                feelsLike = weatherResponse.main?.feels_like.toString(),
                tempMax = weatherResponse.main?.temp_max.toString(),
                tempMin = weatherResponse.main?.temp_min.toString(),
                iconUrl = "https://openweathermap.org/img/wn/${weatherResponse.weather?.firstOrNull()?.icon}@2x.png",
            ),

            city = weatherResponse.name.orEmpty(),
            sunrise = weatherResponse.sys?.sunrise?.let {
                formatTimestamp(it)
            } ?: "",
            sunset = weatherResponse.sys?.sunset?.let {
                formatTimestamp(it)
            } ?: "",
            country = weatherResponse.sys?.country.orEmpty(),

            )
    }

    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }


}