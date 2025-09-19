package com.nilezia.myweather.domain

import com.nilezia.myweather.data.model.WeatherResponse
import com.nilezia.myweather.data.repository.WeatherRepository
import com.nilezia.myweather.domain.model.DailyWeatherUi
import com.nilezia.myweather.domain.model.WeatherUi
import com.nilezia.myweather.extension.toTimeString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
                temperature = weatherResponse.main?.temp?.toString()?:"-",
                humidity = weatherResponse.main?.humidity?.toString()?:"-",
                windSpeed = weatherResponse.wind?.speed?.toString()?:"-",
                description = weatherResponse.weather?.firstOrNull()?.description.orEmpty(),
                feelsLike = weatherResponse.main?.feels_like?.toString()?:"-",
                tempMax = weatherResponse.main?.temp_max?.toString()?:"-",
                tempMin = weatherResponse.main?.temp_min?.toString()?:"-",
                iconUrl = "https://openweathermap.org/img/wn/${weatherResponse.weather?.firstOrNull()?.icon}@2x.png",
            ),

            city = weatherResponse.name.orEmpty(),
            sunrise = weatherResponse.sys?.sunrise?.toTimeString() ?: "",
            sunset = weatherResponse.sys?.sunset?.toTimeString() ?: "",
            country = weatherResponse.sys?.country.orEmpty(),

            )
    }
}