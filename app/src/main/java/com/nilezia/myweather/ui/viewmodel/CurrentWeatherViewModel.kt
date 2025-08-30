package com.nilezia.myweather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilezia.myweather.data.repository.CurrentLocationRepository
import com.nilezia.myweather.domain.GetForecastUseCase
import com.nilezia.myweather.domain.GetWeatherUseCase
import com.nilezia.myweather.domain.model.CurrentWeatherUi
import com.nilezia.myweather.domain.model.WeatherUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val currentLocationRepository: CurrentLocationRepository,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    // ใช้ StateFlow สำหรับ state ของ UI
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun getCurrentWeather() {
        viewModelScope.launch {
            try {
                _uiState.value = WeatherUiState.Loading

                val location = currentLocationRepository.getCurrentLocation()
                val lat = location.latitude
                val lon = location.longitude
                fetchWeatherByLocation(13.8461752070497, 100.84000360685337)
                // fetchWeatherByLocation(lat, lon)
            } catch (e: Exception) {
                fetchWeatherByLocation(13.8461752070497, 100.84000360685337)
                Log.d("TAG", "getCurrentWeather: ${e.message}")
                // handle error เช่น location ไม่ได้ หรือ network error
            }
        }
    }

    private suspend fun fetchWeatherByLocation(lat: Double, lon: Double) {
        getWeatherUseCase.execute(lat, lon)
            .catch { e ->

                Log.d("TAG", "getCurrentWeather: ${e.message}")
            }
            .collect {
                _uiState.value = WeatherUiState.Success(it)
                Log.d("TAG", "getCurrentWeather: $it.")
            }
    }

    fun getForecastWeather() {
        viewModelScope.launch {
            try {
                _uiState.value = WeatherUiState.Loading

                val location = currentLocationRepository.getCurrentLocation()
                val lat = location.latitude
                val lon = location.longitude
                fetchForecastByLocation(13.8461752070497, 100.84000360685337)
                // fetchWeatherByLocation(lat, lon)
            } catch (e: Exception) {
                fetchForecastByLocation(13.8461752070497, 100.84000360685337)
                Log.d("TAG", "getCurrentWeather: ${e.message}")
                // handle error เช่น location ไม่ได้ หรือ network error
            }
        }
    }

    private suspend fun fetchForecastByLocation(lat: Double, lon: Double) {
        getForecastUseCase.execute(lat, lon)
            .catch { e ->

                Log.d("TAG", "getCurrentWeather: ${e.message}")
            }
            .collect {
                //_uiState.value = WeatherUiState.Success(it)
                Log.d("TAG", "getCurrentWeather: $it.")
            }
    }
}

sealed class WeatherUiState {
    object Loading : WeatherUiState()                   // กำลังโหลด
    data class Success(val weather: CurrentWeatherUi) : WeatherUiState() // โหลดเสร็จ
    data class Error(val message: String) : WeatherUiState()            // เกิด error
}