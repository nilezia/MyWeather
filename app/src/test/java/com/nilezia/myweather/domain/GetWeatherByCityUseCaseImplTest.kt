package com.nilezia.myweather.domain

import com.nilezia.myweather.data.model.DirectLocationResponse
import com.nilezia.myweather.data.repository.WeatherRepository
import io.mockk.coEvery
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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetWeatherByCityUseCaseImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: WeatherRepository
    private lateinit var useCase: GetWeatherByCityUseCaseImpl

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        useCase = GetWeatherByCityUseCaseImpl(repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute maps normal response correctly`() = runTest {
        val response = listOf(
            DirectLocationResponse(
                name = "Bangkok",
                localNames = mapOf("en" to "Bangkok"),
                lat = 13.75,
                lon = 100.5,
                country = "TH",
                state = "Bangkok"
            )
        )

        coEvery { repository.getDailyWeatherByCityName(any()) } returns flowOf(response)

        val result = useCase.execute("Bangkok").first()

        assertEquals(1, result.size)
        val item = result[0]
        assertEquals("Bangkok", item.name)
        assertEquals("Bangkok", item.localNames)
        assertEquals(13.75, item.lat)
        assertEquals(100.5, item.lon)
        assertEquals("TH", item.country)
        assertEquals("Bangkok", item.state)
    }

    @Test
    fun `execute filters locations with lat or lon -1`() = runTest {
        val response = listOf(
            DirectLocationResponse("CityA", null, -1.0, 100.0, "TH", null),
            DirectLocationResponse("CityB", null, 13.0, -1.0, "TH", null),
            DirectLocationResponse("CityC", null, 13.0, 100.0, "TH", null)
        )
        coEvery { repository.getDailyWeatherByCityName(any()) } returns flowOf(response)

        val result = useCase.execute("AnyCity").first()

        assertEquals(1, result.size)
        assertEquals("CityC", result[0].name)
    }

    @Test
    fun `execute filters locations from country ID`() = runTest {
        val response = listOf(
            DirectLocationResponse("CityA", null, 13.0, 100.0, "ID", null),
            DirectLocationResponse("CityB", null, 14.0, 101.0, "TH", null)
        )
        coEvery { repository.getDailyWeatherByCityName(any()) } returns flowOf(response)

        val result = useCase.execute("AnyCity").first()

        assertEquals(1, result.size)
        assertEquals("CityB", result[0].name)
    }

    @Test
    fun `execute maps localNames null to empty string`() = runTest {
        val response = listOf(
            DirectLocationResponse("CityA", null, 13.0, 100.0, "TH", null)
        )
        coEvery { repository.getDailyWeatherByCityName(any()) } returns flowOf(response)

        val result = useCase.execute("CityA").first()

        assertEquals("", result[0].localNames)
    }

}