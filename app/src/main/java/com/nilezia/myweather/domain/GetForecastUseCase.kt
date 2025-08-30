package com.nilezia.myweather.domain

import com.nilezia.myweather.data.model.ForcastResponse
import com.nilezia.myweather.data.repository.WeatherRepository
import com.nilezia.myweather.domain.model.ForecastUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetForecastUseCase {
    fun execute(lat: Double, lon: Double): Flow<ForecastUi>
}

class GetForecastUseCaseImpl(private val weatherRepository: WeatherRepository) :
    GetForecastUseCase {

    override fun execute(lat: Double, lon: Double): Flow<ForecastUi> {
        return weatherRepository.getForecast(lat, lon).map {
            mapDataToDomain(it)
        }

    }

    private fun mapDataToDomain(response: ForcastResponse): ForecastUi {
        return ForecastUi()
    }


}