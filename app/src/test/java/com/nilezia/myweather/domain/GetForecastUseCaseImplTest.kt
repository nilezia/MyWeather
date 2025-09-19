package com.nilezia.myweather.domain

import com.nilezia.myweather.data.model.ForcastResponse
import com.nilezia.myweather.data.model.ForcastResponse.Item.Weather
import com.nilezia.myweather.data.model.WeatherResponse
import com.nilezia.myweather.data.repository.WeatherRepository
import com.nilezia.myweather.data.repository.WeatherRepositoryImpl
import com.nilezia.myweather.domain.model.ForecastUi
import com.nilezia.myweather.domain.model.WeatherUi
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetForecastUseCaseImplTest {

    @MockK
    private lateinit var mockWeatherRepository: WeatherRepository

    private lateinit var getForecastUseCase: GetForecastUseCaseImpl

    @BeforeEach
    fun setUp() {
        mockWeatherRepository = mockk()
        getForecastUseCase = GetForecastUseCaseImpl(mockWeatherRepository)
    }

    @Test
    fun `execute should return mapped ForecastUi when repository returns data`() = runTest {
        // Given
        val lat = 13.7563
        val lon = 100.5018
        val mockResponse = createMockForecastResponse()

        coEvery { mockWeatherRepository.getForecast(lat, lon) } returns flowOf(mockResponse)

        // When
        val result = getForecastUseCase.execute(lat, lon).first()

        // Then
        val expectedForecastUi = ForecastUi(
            city = mockResponse.city,
            list = listOf(
                WeatherUi(
                    mainWeather = "Clear",
                    temperature = "30.0",
                    humidity = "60",
                    windSpeed = "5.0",
                    description = "clear sky",
                    feelsLike = "32.0",
                    tempMax = "31.0",
                    tempMin = "29.0",
                    iconUrl = "https://openweathermap.org/img/wn/01d@2x.png"
                )
            )
        )
        assertEquals(expectedForecastUi, result)
    }

    @Test
    fun `execute should return empty list in ForecastUi when repository returns data with no 12PM forecast`() =
        runTest {
            // Given
            val lat = 13.7563
            val lon = 100.5018
            val mockResponse = createMockForecastResponse(include12PM = false) // No 12PM data

            coEvery { mockWeatherRepository.getForecast(lat, lon) } returns flowOf(mockResponse)

            // When
            val result = getForecastUseCase.execute(lat, lon).first()

            // Then
            val expectedForecastUi = ForecastUi(
                city = mockResponse.city,
                list = emptyList()
            )
            assertEquals(expectedForecastUi, result)
        }

    @Test
    @DisplayName("should handle null or empty values from repository gracefully")
    fun `execute should handle null or empty values from repository gracefully`() = runTest {
        // Given
        val lat = 13.7563
        val lon = 100.5018
        val mockResponse = ForcastResponse(
            city = ForcastResponse.City(
                name = "Test City",
                country = "TC",
                coord = ForcastResponse.City.Coord(0.0, 0.0),
                id = 1,
                population = 100,
                sunrise = 0,
                sunset = 0,
                timezone = 0
            ),
            cnt = 1,
            cod = "200",
            message = 0,
            list = listOf(
                ForcastResponse.Item(
                    clouds = null,
                    dt = 1678886400,
                    dt_txt = "2023-03-15 12:00:00", // Item at 12:00:00
                    main = WeatherResponse.Main(
                        temp = null,
                        feels_like = null,
                        grnd_level = 0,
                        humidity = null,
                        pressure = 0,
                        sea_level = 0,
                        temp_max = null,
                        temp_min = null
                    ),
                    pop = 0.0,
                    rain = null,
                    sys = WeatherResponse.Sys(),
                    visibility = 0,
                    weather = listOf(Weather(icon = null, description = null, main = null, id = 0)),
                    wind = null
                )
            )
        )

        coEvery { mockWeatherRepository.getForecast(lat, lon) } returns flowOf(mockResponse)

        // When
        val result = getForecastUseCase.execute(lat, lon).first()

        // Then
        val expectedForecastUi = ForecastUi(
            city = mockResponse.city,
            list = listOf(
                WeatherUi(
                    mainWeather = "",
                    temperature = "-",
                    humidity = "-",
                    windSpeed = "-",
                    description = "",
                    feelsLike = "-",
                    tempMax = "-",
                    tempMin = "-",
                    iconUrl = "https://openweathermap.org/img/wn/null@2x.png"
                )
            )
        )
        assertEquals(expectedForecastUi, result)
    }

    @Test
    @DisplayName("should return empty list in ForecastUi when repository returns null list")
    fun `execute should return empty list in ForecastUi when repository returns null list`() =
        runTest {
            // Given
            val lat = 13.7563
            val lon = 100.5018
            val mockResponse = ForcastResponse(
                city = ForcastResponse.City(
                    name = "Test City",
                    country = "TC",
                    coord = ForcastResponse.City.Coord(0.0, 0.0),
                    id = 1,
                    population = 100,
                    sunrise = 0,
                    sunset = 0,
                    timezone = 0
                ),
                cnt = 0,
                cod = "200",
                message = 0,
                list = null // Null list
            )

            coEvery { mockWeatherRepository.getForecast(lat, lon) } returns flowOf(mockResponse)

            // When
            val result = getForecastUseCase.execute(lat, lon).first()

            // Then
            val expectedForecastUi = ForecastUi(
                city = mockResponse.city,
                list = emptyList()
            )
            assertEquals(expectedForecastUi, result)
        }


    // Helper function to create mock ForcastResponse (เหมือนเดิม)
    private fun createMockForecastResponse(include12PM: Boolean = true): ForcastResponse {
        val listItems = mutableListOf<ForcastResponse.Item>()

        if (include12PM) {
            listItems.add(
                ForcastResponse.Item(
                    clouds = WeatherResponse.Clouds(all = 0),
                    dt = 1678886400, // Example timestamp
                    dt_txt = "2023-03-15 12:00:00",
                    main = WeatherResponse.Main(
                        temp = 30.0,
                        feels_like = 32.0,
                        grnd_level = 1000,
                        humidity = 60,
                        pressure = 1012,
                        sea_level = 1012,

                        temp_max = 31.0,
                        temp_min = 29.0
                    ),
                    pop = 0.1,
                    rain = WeatherResponse.Rain(treeHour = 0.0),
                    sys = WeatherResponse.Sys(),
                    visibility = 10000,
                    weather = listOf(
                        Weather(
                            icon = "01d",
                            description = "clear sky",
                            main = "Clear",
                            id = 800
                        )
                    ),
                    wind = WeatherResponse.Wind(deg = 180, gust = 7.0, speed = 5.0)
                )
            )
        }
        listItems.add(
            ForcastResponse.Item(
                clouds = WeatherResponse.Clouds(all = 20),
                dt = 1678897200,
                dt_txt = "2023-03-15 15:00:00", // Another item not at 12 PM
                main = WeatherResponse.Main(
                    temp = 31.0,
                    feels_like = 33.0,
                    grnd_level = 1000,
                    humidity = 55,
                    pressure = 1011,
                    sea_level = 1011,
                    temp_max = 32.0,
                    temp_min = 30.0
                ),
                pop = 0.0,
                rain = null,
                sys = WeatherResponse.Sys(),
                visibility = 10000,
                weather = listOf(
                    Weather(
                        icon = "02d",
                        description = "few clouds",
                        main = "Clouds",
                        id = 801
                    )
                ),
                wind = WeatherResponse.Wind(deg = 190, gust = 8.0, speed = 6.0)
            )
        )

        return ForcastResponse(
            city = ForcastResponse.City(
                name = "Bangkok",
                country = "TH",
                coord = ForcastResponse.City.Coord(
                    13.7563,
                    100.5018
                ),
                id = 1609350,
                population = 1000000,
                sunrise = 1678839958,
                sunset = 1678883584,
                timezone = 25200
            ),
            cnt = listItems.size,
            cod = "200",
            message = 0,
            list = listItems
        )
    }
}