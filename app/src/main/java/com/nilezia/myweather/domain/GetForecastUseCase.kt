package com.nilezia.myweather.domain

import com.nilezia.myweather.data.model.ForcastResponse
import com.nilezia.myweather.data.repository.WeatherRepository
import com.nilezia.myweather.domain.model.ForecastUi
import com.nilezia.myweather.domain.model.WeatherUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetForecastUseCase {
    fun execute(lat: Double, lon: Double): Flow<ForecastUi>
}

class GetForecastUseCaseImpl(private val weatherRepository: WeatherRepository) :
    GetForecastUseCase {

    override fun execute(lat: Double, lon: Double): Flow<ForecastUi> {
        return weatherRepository.getForecast(lat, lon).map {
                val a= "a"
            mapDataToDomain(it)
        }

    }

    private fun mapDataToDomain(response: ForcastResponse): ForecastUi {
        val dailyForecast = response.list
            ?.filter { item ->
                // เลือกเฉพาะเวลา 12:00:00 ของแต่ละวัน
                item.dt_txt?.contains("12:00:00") == true
            } ?: listOf()
        return ForecastUi(
            city = response.city,
            list = dailyForecast.map {
                WeatherUi(
                    mainWeather = it.weather?.firstOrNull()?.main.orEmpty(),
                    temperature = it.main?.temp.toString(),
                    humidity = it.main?.humidity.toString(),
                    windSpeed = it.wind?.speed.toString(),
                    description = it.weather?.firstOrNull()?.description.orEmpty(),
                    feelsLike = it.main?.feels_like.toString(),
                    tempMax = it.main?.temp_max.toString(),
                    tempMin = it.main?.temp_min.toString(),
                    iconUrl = "https://openweathermap.org/img/wn/${it.weather?.firstOrNull()?.icon}@2x.png",
                )

            }
        )
    }
}