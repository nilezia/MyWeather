package com.nilezia.myweather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilezia.myweather.data.repository.CurrentLocationRepository
import com.nilezia.myweather.domain.GetForecastUseCase
import com.nilezia.myweather.domain.GetWeatherUseCase
import com.nilezia.myweather.domain.model.DailyWeatherUi
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
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    // ใช้ StateFlow สำหรับ state ของ UI
    private val _uiState = MutableStateFlow(WeatherUiState(isLoading = true))
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun getDailyWeather() {
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
                val lat = location.latitude
                val lon = location.longitude
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

    private suspend fun fetchWeatherByLocation(lat: Double, lon: Double) {
        getWeatherUseCase.execute(lat, lon)
            .catch { e ->

                Log.d("TAG", "getDailyWeather: ${e.message}")
            }
            .collect { data->
                _uiState.update {
                    it.copy(
                        dailyWeather = data,
                        isLoading = false,
                    )
                }
            }
    }

    fun getForecastWeather() {
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
                val lat = location.latitude
                val lon = location.longitude
                fetchForecastByLocation(lat, lon)
            } catch (e: Exception) {
                fetchForecastByLocation(13.8461752070497, 100.84000360685337)
                Log.d("TAG", "getDailyWeather: ${e.message}")
            }
        }
    }

    private suspend fun fetchForecastByLocation(lat: Double, lon: Double) {
        getForecastUseCase.execute(lat, lon)
            .catch { e ->

                Log.d("TAG", "getDailyWeather: ${e.message}")
            }
            .collect { data->
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
    val forecast: ForecastUi? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

