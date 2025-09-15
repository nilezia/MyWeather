package com.nilezia.myweather.ui.navigate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nilezia.myweather.ui.screen.DailyWeatherScreen
import com.nilezia.myweather.ui.screen.ForecastScreen
import com.nilezia.myweather.ui.screen.OtherWeatherScreen
import com.nilezia.myweather.ui.viewmodel.WeatherViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
) {

    val viewModel: WeatherViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val topBarTitle = when (currentRoute) {
        Screen.DailyWeather.route -> uiState.dailyWeather?.city ?: ""
        Screen.ForecastWeather.route -> "Forecast"
        Screen.OtherWeather.route -> "Other Locations"
        else -> ""
    }
    // Dynamic actions
    val topBarActions: @Composable RowScope.() -> Unit = {
        when (currentRoute) {
            Screen.DailyWeather.route -> {
                IconButton(onClick = {
                    viewModel.getDailyWeather()
                    viewModel.getForecastWeather()

                }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { navController.navigate(Screen.OtherWeather.route) }) {
                    Icon(
                        Icons.AutoMirrored.Filled.List,
                        contentDescription = "Other Locations",
                        tint = Color.White
                    )
                }
            }

            Screen.ForecastWeather.route -> {}

            Screen.OtherWeather.route -> { }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF90CAF9),
                        Color(0xFF2196F3),
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(topBarTitle) },
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    tint = Color.White,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    actions = topBarActions,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            },
            content = { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.DailyWeather.route
                ) {
                    composable(Screen.DailyWeather.route) {
                        Column(modifier = Modifier.padding(padding)) {
                            DailyWeatherScreen(viewModel, navController)

                        }
                    }

                    // Forecast Detail Screen
                    composable(Screen.ForecastWeather.route) {
                        Column(modifier = Modifier.padding(padding)) {
                           // ForecastScreen(viewModel)

                        }
                    }

                    composable(Screen.OtherWeather.route) {
                        Column(modifier = Modifier.padding(padding)) {
                            OtherWeatherScreen(navController = navController, onSelect = {
                                viewModel.getDailyWeather(it.lat, it.lon)
                                viewModel.getForecastWeather(it.lat, it.lon)
                            })
                        }
                    }
                }
            }
        )
    }
}

sealed class Screen(val route: String) {
    object DailyWeather : Screen("daily_weather")
    object ForecastWeather : Screen("forecast")
    object OtherWeather : Screen("other_weather")
}