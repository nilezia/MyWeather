## 🌦️ My Weather (Android Native)

A simple native Android (Kotlin) weather app that shows current weather and 5-day forecast (items at 12:00).
Uses OpenWeather API, Retrofit + Coroutines, Hilt for DI, MVVM (ViewModel + StateFlow).

## 🚀 Features

- 🌤️ Display current weather by location  
- 📅 5-day forecast at 12:00 PM daily  
- 🗺️ Fetch data from OpenWeather API  
- 💧 Show temperature, humidity, wind speed  
- 🎨 Clean UI with gradient background


🧠 Architecture Overview
UseCase → Repository → API → Model → UI

🧩 Tech Stack

Retrofit + Gson to call OpenWeather API
Kotlin Coroutines + Flow / StateFlow
Hilt for Dependency Injection
Simple MVVM architecture
Runtime location permission + Internet permission

📸 Screenshots
![ezgif-8fd7402cd8e3a9](https://github.com/user-attachments/assets/688de3ab-6896-4a37-bb52-8ef7a7c6344c)
