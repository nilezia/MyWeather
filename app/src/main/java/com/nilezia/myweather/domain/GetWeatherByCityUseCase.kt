package com.nilezia.myweather.domain

import com.nilezia.myweather.data.model.DirectLocationResponse
import com.nilezia.myweather.data.repository.WeatherRepository
import com.nilezia.myweather.domain.model.DirectLocationUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject

interface GetWeatherByCityUseCase {
    fun execute(cityName: String): Flow<List<DirectLocationUi>>
}

class GetWeatherByCityUseCaseImpl @Inject constructor(private val weatherRepository: WeatherRepository) :
    GetWeatherByCityUseCase {
    override fun execute(
        cityName: String
    ): Flow<List<DirectLocationUi>> {
        return weatherRepository.getDailyWeatherByCityName(cityName)
            .map { response ->
                mapDataToDomain(response).filter { it.lat != -1.0 && it.lon != -1.0 }.filter { it.country !="ID" }
            }
    }

    private fun mapDataToDomain(response: List<DirectLocationResponse>): List<DirectLocationUi> {
        return response.map {
            DirectLocationUi(
                name = it.name,
                localNames = getLocalName(it.localNames, Locale.getDefault()),
                lat = it.lat,
                lon = it.lon,
                country = it.country,
                state = it.state ?: ""
            )
        }

    }

   private fun getLocalName(localNames: Map<String, String>?, locale: Locale): String {
        return localNames?.get(locale.language)
            ?: localNames?.get("en")
            ?: "Unknown"
    }

}