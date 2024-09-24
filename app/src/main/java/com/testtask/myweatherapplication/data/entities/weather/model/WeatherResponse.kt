package com.testtask.myweatherapplication.data.entities.weather.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
	val coord: Coord,
	val weather: List<WeatherCondition>,
	val base: String,
	val main: MainWeatherData,
	val visibility: Int,
	val wind: Wind,
	val clouds: Clouds,
	val dt: Long,
	val sys: Sys,
	val timezone: Int,
	val id: Long,
	val name: String,
	val cod: Int
)
