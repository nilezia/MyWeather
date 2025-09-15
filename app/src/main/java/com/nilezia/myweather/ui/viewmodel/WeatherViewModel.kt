package com.nilezia.myweather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilezia.myweather.data.repository.CurrentLocationRepository
import com.nilezia.myweather.domain.GetForecastUseCase
import com.nilezia.myweather.domain.GetWeatherByCityUseCase
import com.nilezia.myweather.domain.GetWeatherUseCase
import com.nilezia.myweather.domain.model.DailyWeatherUi
import com.nilezia.myweather.domain.model.DirectLocationUi
import com.nilezia.myweather.domain.model.ForecastUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val currentLocationRepository: CurrentLocationRepository,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState(isLoading = true))
    val uiState: StateFlow<WeatherUiState> = _uiState

    private var isWeatherLoaded = false

    fun loadWeatherIfNeeded() {
        if (!isWeatherLoaded) {
            getDailyWeather()
            getForecastWeather()
            isWeatherLoaded = true
        }
    }
    fun getDailyWeather(latSearch: Double? = null, longSearch: Double? = null) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        dailyWeather = null,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                val location = currentLocationRepository.getCurrentLocation()
                val lat = latSearch ?: location.latitude
                val lon = longSearch ?: location.longitude

                fetchWeatherByLocation(lat, lon)


            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        dailyWeather = null,
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                Log.d("TAG", "getDailyWeather: ${e.message}")
            }
        }
    }

    suspend fun fetchWeatherByLocation(lat: Double, lon: Double) {
        getWeatherUseCase.execute(lat, lon)
            .catch { e ->

                Log.d("TAG", "getDailyWeather: ${e.message}")
            }
            .collect { data ->
                _uiState.update {
                    it.copy(
                        dailyWeather = data,
                        isLoading = false,
                    )
                }
            }
    }

    fun getForecastWeather(latSearch: Double? = null, longSearch: Double? = null) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        dailyWeather = null,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                val location = currentLocationRepository.getCurrentLocation()
                val lat = latSearch ?: location.latitude
                val lon = longSearch ?: location.longitude
                fetchForecastByLocation(lat, lon)
            } catch (e: Exception) {
                fetchForecastByLocation(13.8461752070497, 100.84000360685337)
                Log.d("TAG", "getDailyWeather: ${e.message}")
            }
        }
    }

    fun getWeatherByCityName(cityName: String) {
        viewModelScope.launch {
            getWeatherByCityUseCase.execute(cityName).catch {

            }.collect { data ->
                _uiState.update {
                    it.copy(
                        listLocationUi = data,
                        isLoading = false,
                    )
                }
                Log.d("TAG", "getWeatherByCityName: $data.")
            }
        }
    }

    private suspend fun fetchForecastByLocation(lat: Double, lon: Double) {
        getForecastUseCase.execute(lat, lon)
            .catch { e ->

                Log.d("TAG", "getDailyWeather: ${e.message}")
            }
            .collect { data ->
                _uiState.update {
                    it.copy(
                        forecast = data,
                        isLoading = false,
                    )
                }
            }
    }
}

data class WeatherUiState(
    val dailyWeather: DailyWeatherUi? = null,
    val listLocationUi: List<DirectLocationUi>? = null,
    val forecast: ForecastUi? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

