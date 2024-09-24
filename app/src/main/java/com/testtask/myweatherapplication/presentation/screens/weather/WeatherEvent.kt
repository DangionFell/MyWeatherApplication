package com.testtask.myweatherapplication.presentation.screens.weather

import com.testtask.myweatherapplication.domain.model.other.Location

sealed interface WeatherEvent {
	data class ResolveWeatherSource(val isPermissionGranted: Boolean) : WeatherEvent
	data class GetWeatherForecast(val location: Location) : WeatherEvent
	data object OnDismissError : WeatherEvent
}