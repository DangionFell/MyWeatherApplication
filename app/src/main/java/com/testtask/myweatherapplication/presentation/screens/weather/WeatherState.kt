package com.testtask.myweatherapplication.presentation.screens.weather

import com.testtask.myweatherapplication.data.entities.weather.model.WeatherForecast
import com.testtask.myweatherapplication.domain.model.weather.Forecast
import com.testtask.myweatherapplication.domain.model.weather.SimpleDayForecast
import com.testtask.myweatherapplication.domain.model.weather.Weather

data class WeatherState(
	val isLoading: Boolean = true,
	val weather: Weather? = null,
	val simpleForecast: List<SimpleDayForecast>? = null,
	val detailedForecast: List<List<WeatherForecast>>? = null,
	val errorMessage: String? = null,
)
