package com.nilezia.myweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nilezia.myweather.domain.model.CurrentWeatherUi
import com.nilezia.myweather.ui.screen.CurrentWeatherScreen
import com.nilezia.myweather.ui.screen.ForecastScreen
import com.nilezia.myweather.ui.screen.MainCurrentWeather
import com.nilezia.myweather.ui.theme.MyWeatherTheme
import com.nilezia.myweather.ui.viewmodel.CurrentWeatherViewModel
import com.nilezia.myweather.ui.viewmodel.WeatherUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var weather: CurrentWeatherUi by mutableStateOf(CurrentWeatherUi())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWeatherTheme {
                val viewModel: CurrentWeatherViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()
                WeatherScreen(uiState, onRefresh = { viewModel.getCurrentWeather() })
                LaunchedEffect(Unit) {
                    viewModel.getCurrentWeather()
                }
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WeatherScreen(uiState: WeatherUiState, onRefresh: () -> Unit = {}) {
        InitialUiState(uiState)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF90CAF9),  // ฟ้าอ่อน
                            Color(0xFF2196F3),  // ฟ้าเข้ม
                        )
                    )
                )
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = { Text("${weather.city}") },
                        actions = {
                            IconButton(onClick = { onRefresh() }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh",  tint = Color.White)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent, // โปร่งใส
                            titleContentColor = Color.White
                        )
                    )
                },
                content = { padding ->
                    Column(modifier = Modifier.padding(padding)) {

                        CurrentWeatherScreen(weather)

                    }
                }
            )
        }

    }

    @Composable
    fun InitialUiState(uiState: WeatherUiState) {
        when (uiState) {
            is WeatherUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is WeatherUiState.Success -> {
                 weather = uiState.weather
                CurrentWeatherScreen(weather)
            }

            is WeatherUiState.Error -> {
                val message = uiState.message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Error: $message", color = Color.Red)
                }
            }
        }
    }


    @Composable
    fun WeatherTabs(tabs: List<String>, selectedTab: Int, onTabSelected: (Int) -> Unit) {
        TabRow(
            selectedTabIndex = selectedTab,
            contentColor = Color.White,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .padding(horizontal = 16.dp)
                        .background(Color.White)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    text = { Text(title) }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun WeatherScreenPreview() {
        // ตัวอย่าง mock data
        val mockWeather = WeatherUiState.Success(
            weather = CurrentWeatherUi(
                city = "หนองจอก",
                temperature = "4.33",
                mainWeather = "ฝนตกหนัก",
                sunset = "17:00",
                sunrise = "06:00",
                country = "TH",
                windSpeed = "1.5",
                humidity = "80",
                description = "ฝนตกหนัก",
                iconUrl = "https://openweathermap.org/img/wn/10d@2x.png"


            )
        )

        MyWeatherTheme {
            WeatherScreen(
                uiState = mockWeather
            )
        }

    }
}