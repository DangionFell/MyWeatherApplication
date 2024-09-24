package com.testtask.myweatherapplication.presentation.screens.weather.model

import com.testtask.myweatherapplication.domain.model.other.Location

data class WeatherScreenParams(
	val cityName: String,
	val location: Location
)
