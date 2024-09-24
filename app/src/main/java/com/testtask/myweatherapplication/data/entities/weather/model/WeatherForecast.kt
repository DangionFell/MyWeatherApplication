package com.testtask.myweatherapplication.data.entities.weather.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecast(
	val dt: Long,
	val main: MainWeatherData,
	val weather: List<WeatherCondition>,
	val clouds: Clouds,
	val wind: Wind,
	val visibility: Int,
	val pop: Double,
	val rain: Rain? = null,
	val sys: ForecastSys,
	@SerialName("dt_txt") val dateTimeText: String
)

