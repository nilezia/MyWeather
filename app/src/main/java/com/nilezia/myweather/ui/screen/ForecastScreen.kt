package com.nilezia.myweather.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.nilezia.myweather.domain.model.ForecastUi
import com.nilezia.myweather.ui.theme.MyWeatherTheme
import com.nilezia.myweather.ui.viewmodel.WeatherTypeUiState

@Composable
fun ForecastScreen(forecastUi: ForecastUi) {

    Column(modifier = Modifier.fillMaxWidth()){
        Text(text = "ForecastScreen")
        Text(text = forecastUi.toString())
    }
}
@Preview(showBackground = true)
@Composable
fun ForecastScreenPreview(){
    val mockWeather = WeatherTypeUiState.ForecastWeatherState(ForecastUi())
    MyWeatherTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF90CAF9),  // ฟ้าอ่อน
                            Color(0xFF2196F3),  // ฟ้าเข้ม
                        )
                    )
                )
        ) {
            ForecastScreen(mockWeather.forecast)
        }
    }

}