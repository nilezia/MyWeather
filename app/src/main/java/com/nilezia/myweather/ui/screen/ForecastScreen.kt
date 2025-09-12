package com.nilezia.myweather.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.nilezia.myweather.domain.model.ForecastUi
import com.nilezia.myweather.ui.theme.MyWeatherTheme
import com.nilezia.myweather.ui.viewmodel.WeatherViewModel

@Composable
fun ForecastScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF90AAF9),
                        Color(0xFF2196F3),
                    )
                )
            )
    )
    Toast.makeText(context, "ForecastScreen", Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun ForecastScreenPreview() {
    val mockWeather = ForecastUi()
    MyWeatherTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            ForecastScreen()
        }
    }
}