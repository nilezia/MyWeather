package com.nilezia.myweather.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.nilezia.myweather.ui.theme.MyWeatherTheme
import com.nilezia.myweather.ui.viewmodel.WeatherUiState

@Composable
fun CurrentWeatherScreen(uiState: CurrentWeatherUi) {
    MainCurrentWeather(uiState)

}

@Composable
fun MainCurrentWeather(weather: CurrentWeatherUi) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
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
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "${weather.temperature.toDouble().toInt()}°C",
            fontSize = 64.sp,
            color = Color.White
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = weather.description,
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
        }
        Temperature(weather, modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // เว้นระยะระหว่างการ์ด
        ) {
            WindCard(weather, modifier = Modifier.weight(1f))
            SunCard(weather, modifier = Modifier.weight(1f))
        }

    }

}

@Composable
fun Temperature(weather: CurrentWeatherUi, modifier: Modifier) {
    Column (modifier = modifier){
        Row {
            Text(text = "↑${weather.tempMax.toDouble().toInt()}°", fontSize = 22.sp, color = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "↓${weather.tempMin.toDouble().toInt()}°",  fontSize = 22.sp, color = Color.White)
        }
        Text(text = "อุณหภูมิที่รู้สึกได้ ${weather.feelsLike}°",  fontSize = 22.sp, color = Color.White)
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
                    text = "${weather.windSpeed} km/h",
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
                    text = "${weather.humidity} %",
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
            MainCurrentWeather(mockWeather.weather)
        }
    }
}

