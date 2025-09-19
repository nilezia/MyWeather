package com.nilezia.myweather.domain

import com.nilezia.myweather.data.model.WeatherResponse
import com.nilezia.myweather.data.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetWeatherUseCaseImplTest {

    private val testDispatcher = StandardTestDispatcher()
    @MockK
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var useCase: GetWeatherUseCaseImpl

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        weatherRepository = mockk()
        useCase = GetWeatherUseCaseImpl(weatherRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

   @Test
    fun `execute should map WeatherResponse to DailyWeatherUi correctly`() = runTest {
        // Arrange
        val fakeResponse = WeatherResponse(
            weather = listOf(
                WeatherResponse.Weather(description = "clear sky", main = "Clear", icon = "01d")
            ),
            main = WeatherResponse.Main(
                temp = 30.0,
                feels_like = 32.0,
                temp_min = 25.0,
                temp_max = 35.0,
                humidity = 60
            ),
            wind = WeatherResponse.Wind(speed = 5.0),
            sys = WeatherResponse.Sys(sunrise = 1660000000, sunset = 1660040000, country = "TH"),
            name = "Bangkok"
        )

        coEvery { weatherRepository.getDailyWeather(any(), any()) } returns flowOf(fakeResponse)

        // Act
        val result = useCase.execute(13.75, 100.5).first()

        // Assert
        assertEquals("Clear", result.weatherUi.mainWeather)
        assertEquals("30.0", result.weatherUi.temperature)
        assertEquals("60", result.weatherUi.humidity)
        assertEquals("Bangkok", result.city)
        assertEquals("TH", result.country)
        assertTrue(result.weatherUi.iconUrl.contains("01d@2x.png"))
    }

}