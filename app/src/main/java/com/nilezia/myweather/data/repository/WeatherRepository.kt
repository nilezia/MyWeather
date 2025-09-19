package com.nilezia.myweather.data.repository

import com.google.gson.Gson
import com.nilezia.myweather.data.api.ApiService
import com.nilezia.myweather.data.model.DirectLocationResponse
import com.nilezia.myweather.data.model.ForcastResponse
import com.nilezia.myweather.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface WeatherRepository {
    fun getDailyWeather(lat: Double, lon: Double): Flow<WeatherResponse>
    fun getForecast(lat: Double, lon: Double): Flow<ForcastResponse>

    fun getDailyWeatherByCityName(cityName: String): Flow<List<DirectLocationResponse>>


}

class WeatherRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    WeatherRepository {
    override fun getDailyWeather(lat: Double, lon: Double): Flow<WeatherResponse> {
        return flow {
            try {
                  val response = apiService.getDailyWeather(lat, lon)
                  if (response.isSuccessful) {
                      response.body()?.let {
                          emit(it)
                      }
                  } else {
                      throw Exception("Failed to fetch weather data")
                  }
                /*val json = "{\n" +
                        "    \"coord\": {\n" +
                        "        \"lon\": 100.84,\n" +
                        "        \"lat\": 13.8462\n" +
                        "    },\n" +
                        "    \"weather\": [\n" +
                        "        {\n" +
                        "            \"id\": 804,\n" +
                        "            \"main\": \"Clouds\",\n" +
                        "            \"description\": \"เมฆเต็มท้องฟ้า\",\n" +
                        "            \"icon\": \"04d\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"base\": \"stations\",\n" +
                        "    \"main\": {\n" +
                        "        \"temp\": 30.63,\n" +
                        "        \"feels_like\": 37.63,\n" +
                        "        \"temp_min\": 30.63,\n" +
                        "        \"temp_max\": 30.63,\n" +
                        "        \"pressure\": 1010,\n" +
                        "        \"humidity\": 78,\n" +
                        "        \"sea_level\": 1010,\n" +
                        "        \"grnd_level\": 1009\n" +
                        "    },\n" +
                        "    \"visibility\": 10000,\n" +
                        "    \"wind\": {\n" +
                        "        \"speed\": 4.08,\n" +
                        "        \"deg\": 231,\n" +
                        "        \"gust\": 7.77\n" +
                        "    },\n" +
                        "    \"clouds\": {\n" +
                        "        \"all\": 100\n" +
                        "    },\n" +
                        "    \"dt\": 1756263438,\n" +
                        "    \"sys\": {\n" +
                        "        \"country\": \"TH\",\n" +
                        "        \"sunrise\": 1756249495,\n" +
                        "        \"sunset\": 1756294320\n" +
                        "    },\n" +
                        "    \"timezone\": 25200,\n" +
                        "    \"id\": 1608258,\n" +
                        "    \"name\": \"หนองจอก\",\n" +
                        "    \"cod\": 200\n" +
                        "}"
                val weatherResponse = Gson().fromJson(json, WeatherResponse::class.java)
                emit(weatherResponse)*/
            } catch (e: Exception) {
                throw Exception("${e.message}")
            }
        }
    }

    override fun getForecast(
        lat: Double,
        lon: Double
    ): Flow<ForcastResponse> {
        return flow {
            val response = apiService.getForecastWeather(lat, lon)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it)
                }
            } else {
                throw Exception("Failed to fetch weather data")
            }
        }


    }

    override fun getDailyWeatherByCityName(cityName: String): Flow<List<DirectLocationResponse>> {
        return flow {
            val response = apiService.getWeatherByCityName(cityName)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it)
                }
            } else {
                throw Exception("Failed to fetch weather data")
            }
        }
    }
}