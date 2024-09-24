package com.testtask.myweatherapplication.data.entities.weather.model

import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
	val cod: String,
	val message: Int,
	val cnt: Int,
	val list: List<WeatherForecast>,
	val city: City
)
