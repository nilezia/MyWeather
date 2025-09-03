package com.nilezia.myweather.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nilezia.myweather.R
import com.nilezia.myweather.domain.model.CurrentWeatherUi
import com.nilezia.myweather.domain.model.ForecastUi
import com.nilezia.myweather.domain.model.WeatherUi
import com.nilezia.myweather.ui.screen.mock.weatherUiMock
import com.nilezia.myweather.ui.theme.MyWeatherTheme
import com.nilezia.myweather.ui.viewmodel.WeatherTypeUiState

@Composable
fun CurrentWeatherScreen(currentWeatherUi: CurrentWeatherUi, forecastUi: ForecastUi) {
    MainCurrentWeather(currentWeatherUi, forecastUi)

}

@Composable
fun MainCurrentWeather(weather: CurrentWeatherUi, forecastUi: ForecastUi) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)

                )
                Text(
                    text = weather.city,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    color = Color.White
                )
            }
        }
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "${weather.weatherUi.temperature.toDouble().toInt()}°C",
                fontSize = 64.sp,
                color = Color.White
            )

        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    text = weather.weatherUi.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )

                AsyncImage(
                    model = weather.weatherUi.iconUrl,
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(start = 16.dp) // กำหนดขนาดไอคอน
                )
            }
            Temperature(weather.weatherUi, modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // เว้นระยะระหว่างการ์ด
            ) {
                WindCard(weather, modifier = Modifier.weight(1f))
                SunCard(weather, modifier = Modifier.weight(1f))
            }
        }
        item {
            ForecastWidget(forecastUi, modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
        }
    }


}

@Composable
fun Temperature(weather: WeatherUi, modifier: Modifier) {
    Column(modifier = modifier) {
        Row {
            Text(
                text = "↑${weather.tempMax.toDouble().toInt()}°",
                fontSize = 22.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "↓${weather.tempMin.toDouble().toInt()}°",
                fontSize = 22.sp,
                color = Color.White
            )
        }
        Text(
            text = "อุณหภูมิที่รู้สึกได้ ${weather.feelsLike}°",
            fontSize = 22.sp,
            color = Color.White
        )
    }
}

@Composable
fun WindCard(weather: CurrentWeatherUi, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f) // โปร่งนิด ๆ ให้เห็นพื้นหลัง
        )
    ) {
        Column {
            Row(
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.outline_air_24),
                    contentDescription = "Location Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)

                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    text = "${weather.weatherUi.windSpeed} km/h",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.outline_humidity_mid_24),
                    contentDescription = "humidity Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)

                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    text = "${weather.weatherUi.humidity} %",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }

    }

}

@Composable
fun SunCard(weather: CurrentWeatherUi, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column {
            Row(
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.outline_sunny_24),
                    contentDescription = "Location Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)

                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    text = weather.sunrise,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
            Row(
                modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.outline_sunny_24),
                    contentDescription = "Location Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)

                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    text = weather.sunset,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ForecastWidget(forecastUi: ForecastUi, modifier: Modifier) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f) // โปร่งนิด ๆ ให้เห็นพื้นหลัง
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "5 Day Forecast",
                fontSize = 22.sp,
                color = Color.White
            )
            HorizontalDivider(
                color = DividerDefaults.color,      // สีมาตรฐานตามธีม
                thickness = DividerDefaults.Thickness, // ความหนามาตรฐาน (1.dp)
                modifier = Modifier.padding(vertical = 8.dp)
            )
            DailyForecastScreen(forecastUi.list)
        }
    }
}

@Composable
fun DailyForecastScreen(forecastList: List<WeatherUi>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        contentPadding = PaddingValues(16.dp),

        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(forecastList) { weather ->
            ForecastCard(weather)
        }
    }
}

@Composable
fun ForecastCard(weather: WeatherUi) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {

        Text(
            text = weather.description,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.weight(2f)
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.outline_humidity_mid_24),
            contentDescription = "humidity Icon",
            tint = Color.White,
            modifier = Modifier.size(24.dp)

        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            text = "${weather.humidity} %",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        AsyncImage(
            model = weather.iconUrl,
            contentDescription = "Weather Icon",
            modifier = Modifier
                .size(64.dp)
                .padding(start = 16.dp) // กำหนดขนาดไอคอน
        )
        Text(
            text = " ${weather.temperature}°C",
            fontSize = 14.sp,
            color = Color.White,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    val mockWeather = WeatherTypeUiState.CurrentWeatherState(weatherUiMock())
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
            //    MainCurrentWeather(mockWeather.weather)

        }
    }
}

