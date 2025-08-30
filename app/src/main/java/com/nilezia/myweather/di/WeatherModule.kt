package com.nilezia.myweather.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.nilezia.myweather.data.api.ApiService
import com.nilezia.myweather.data.repository.CurrentLocationRepository
import com.nilezia.myweather.data.repository.CurrentLocationRepositoryImpl
import com.nilezia.myweather.data.repository.WeatherRepository
import com.nilezia.myweather.data.repository.WeatherRepositoryImpl
import com.nilezia.myweather.domain.GetForecastUseCase
import com.nilezia.myweather.domain.GetForecastUseCaseImpl
import com.nilezia.myweather.domain.GetWeatherUseCase
import com.nilezia.myweather.domain.GetWeatherUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
 object WeatherModule {

    @Provides
    fun providesGetWeatherUseCase(weatherRepository: WeatherRepository): GetWeatherUseCase {
        return GetWeatherUseCaseImpl(weatherRepository)
    }
    @Provides
    fun providesGetForecastUseCase(weatherRepository: WeatherRepository): GetForecastUseCase {
        return GetForecastUseCaseImpl(weatherRepository)
    }
     @Provides
    fun providesWeatherRepository(apiService: ApiService): WeatherRepository{
        return WeatherRepositoryImpl(apiService)
    }
    @Provides
    fun providesCurrentLocationRepository(fusedLocationProviderClient: FusedLocationProviderClient): CurrentLocationRepository{
        return CurrentLocationRepositoryImpl(fusedLocationProviderClient)
    }

}