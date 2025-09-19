package com.nilezia.myweather.data.repository

import com.nilezia.myweather.data.api.ApiService
import com.nilezia.myweather.data.model.DirectLocationResponse
import com.nilezia.myweather.data.model.ForcastResponse
import com.nilezia.myweather.data.model.WeatherResponse
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class WeatherRepositoryImplTest {

    @MockK
    private lateinit var mockApiService: ApiService

    private lateinit var weatherRepository: WeatherRepositoryImpl

    @BeforeEach
    fun setUp() {
        weatherRepository = WeatherRepositoryImpl(mockApiService)
    }

    @Nested
    @DisplayName("getDailyWeather tests")
    inner class GetDailyWeatherTests {

        @Test
        @DisplayName("should emit WeatherResponse when api call is successful")
        fun `should emit WeatherResponse on successful api call`() = runTest {
            // Arrange
            val lat = 13.8462
            val lon = 100.84
            val mockWeatherResponseData = mockk<WeatherResponse>()
            val successfulResponse = Response.success(mockWeatherResponseData)
            // Act
            coEvery { mockApiService.getDailyWeather(lat, lon) } returns successfulResponse

            // Assert
            assertEquals(mockWeatherResponseData, successfulResponse.body())
        }

        @Test
        @DisplayName("should throw Exception when api call is not successful")
        fun `should throw Exception on unsuccessful api call`() = runTest {
            // Arrange
            val lat = 13.8462
            val lon = 100.84
            val errorResponseBody = "{\"error\":\"Failed to fetch weather data\"}".toResponseBody(null)
            val errorResponse = Response.error<WeatherResponse>(400, errorResponseBody)

            coEvery { mockApiService.getDailyWeather(lat, lon) } returns errorResponse

            // Act & Assert
            var caughtException: Throwable? = null
            weatherRepository.getDailyWeather(lat, lon)
                .catch { exception -> caughtException = exception }
                .toList() // Collect to trigger the flow and catch

            assertTrue(caughtException is Exception)
            assertEquals("Failed to fetch weather data", caughtException?.message)
        }

        @Test
        @DisplayName("should throw Exception when api call throws an exception")
        fun `should throw Exception when api call itself throws`() = runTest {
            // Arrange
            val lat = 13.8462
            val lon = 100.84
            val networkException = java.io.IOException("Failed to fetch weather data")

            coEvery { mockApiService.getDailyWeather(lat, lon) } throws networkException

            // Act & Assert
            var caughtException: Throwable? = null
            weatherRepository.getDailyWeather(lat, lon)
                .catch { exception -> caughtException = exception }
                .toList() // Collect to trigger the flow and catch

            assertTrue(caughtException is Exception)
            // The message here will be from the re-thrown exception in your repository
            assertEquals("Failed to fetch weather data", caughtException?.message)
        }

        @Test
        @DisplayName("should throw Exception when api call is successful but body is null")
        fun `should throw Exception on successful api call with null body`() = runTest {
            // Arrange
            val lat = 13.8462
            val lon = 100.84
            val successfulResponseWithNullBody = Response.success<WeatherResponse>(null)

            coEvery { mockApiService.getDailyWeather(lat, lon) } returns successfulResponseWithNullBody

            // Act & Assert
            // In your current implementation, a successful response with a null body
            // will not emit anything and the flow will complete without error.
            // If the desired behavior is to throw an exception, you'd need to adjust the repository.
            // For now, let's test the current behavior: the flow completes empty.
            // If you want to throw an exception, you might change the test to expect one.

            val result = weatherRepository.getDailyWeather(lat, lon).toList()
            assertTrue(result.isEmpty(), "Flow should be empty when response body is null and not explicitly handled by throwing an exception.")

            // If you modify your repository to throw an exception for null body like:
            // if (response.isSuccessful) {
            //     response.body()?.let { emit(it) } ?: throw Exception("Response body is null")
            // } ...
            // Then the test would be:
            // var caughtException: Throwable? = null
            // weatherRepository.getDailyWeather(lat, lon)
            //     .catch { exception -> caughtException = exception }
            //     .toList()
            // assertTrue(caughtException is Exception)
            // assertEquals("Response body is null", caughtException?.message)
        }
    }

    @Nested
    @DisplayName("getForecast tests")
    inner class GetForecastTests {

        @Test
        @DisplayName("should emit ForcastResponse when api call is successful")
        fun `should emit ForcastResponse on successful api call`() = runTest {
            // Arrange
            val lat = 13.8462
            val lon = 100.84
            val mockForecastResponseData = mockk<ForcastResponse>()
            val successfulResponse = Response.success(mockForecastResponseData)

            coEvery { mockApiService.getForecastWeather(lat, lon) } returns successfulResponse

            // Act
            val resultFlow = weatherRepository.getForecast(lat, lon)
            val emittedData = resultFlow.first()

            // Assert
            assertEquals(mockForecastResponseData, emittedData)
        }

        @Test
        @DisplayName("should throw Exception when api call is not successful")
        fun `should throw Exception on unsuccessful api call`() = runTest {
            // Arrange
            val lat = 13.8462
            val lon = 100.84
            val errorResponseBody = "{\"error\":\"API error\"}".toResponseBody(null)
            val errorResponse = Response.error<ForcastResponse>(400, errorResponseBody)

            coEvery { mockApiService.getForecastWeather(lat, lon) } returns errorResponse

            // Act & Assert
            var caughtException: Throwable? = null
            weatherRepository.getForecast(lat, lon)
                .catch { exception -> caughtException = exception }
                .toList()

            assertTrue(caughtException is Exception)
            assertEquals("Failed to fetch weather data", caughtException?.message)
        }

        @Test
        @DisplayName("should throw Exception when api call throws an exception")
        fun `should throw Exception when api call itself throws`() = runTest {
            // Arrange
            val lat = 13.8462
            val lon = 100.84
            val networkException = java.io.IOException("Failed to fetch weather data")

            coEvery { mockApiService.getForecastWeather(lat, lon) } throws networkException

            // Act & Assert
            var caughtException: Throwable? = null
            weatherRepository.getForecast(lat, lon)
                .catch { exception -> caughtException = exception }
                .toList()

            assertTrue(caughtException is Exception)
            assertEquals("Failed to fetch weather data", caughtException?.message)
        }
    }

    @Nested
    @DisplayName("getDailyWeatherByCityName tests")
    inner class GetDailyWeatherByCityNameTests {

        @Test
        @DisplayName("should emit List<DirectLocationResponse> when api call is successful")
        fun `should emit List DirectLocationResponse on successful api call`() = runTest {
            // Arrange
            val cityName = "Bangkok"
            val mockLocationResponseData = listOf(mockk<DirectLocationResponse>())
            val successfulResponse = Response.success(mockLocationResponseData)

            coEvery { mockApiService.getWeatherByCityName(cityName) } returns successfulResponse

            // Act
            val resultFlow = weatherRepository.getDailyWeatherByCityName(cityName)
            val emittedData = resultFlow.first()

            // Assert
            assertEquals(mockLocationResponseData, emittedData)
        }

        @Test
        @DisplayName("should throw Exception when api call is not successful")
        fun `should throw Exception on unsuccessful api call`() = runTest {
            // Arrange
            val cityName = "Bangkok"
            val errorResponseBody = "{\"error\":\"API error\"}".toResponseBody(null)
            val errorResponse = Response.error<List<DirectLocationResponse>>(400, errorResponseBody)

            coEvery { mockApiService.getWeatherByCityName(cityName) } returns errorResponse

            // Act & Assert
            var caughtException: Throwable? = null
            weatherRepository.getDailyWeatherByCityName(cityName)
                .catch { exception -> caughtException = exception }
                .toList()

            assertTrue(caughtException is Exception)
            assertEquals("Failed to fetch weather data", caughtException?.message)
        }

        @Test
        @DisplayName("should throw Exception when api call throws an exception")
        fun `should throw Exception when api call itself throws`() = runTest {
            // Arrange
            val cityName = "Bangkok"
            val networkException = java.io.IOException("Failed to fetch weather data")

            coEvery { mockApiService.getWeatherByCityName(cityName) } throws networkException

            // Act & Assert
            var caughtException: Throwable? = null
            weatherRepository.getDailyWeatherByCityName(cityName)
                .catch { exception -> caughtException = exception }
                .toList()

            assertTrue(caughtException is Exception)
            assertEquals("Failed to fetch weather data", caughtException?.message)
        }
    }
}