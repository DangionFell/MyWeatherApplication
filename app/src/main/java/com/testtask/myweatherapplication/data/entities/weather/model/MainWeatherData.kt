package com.testtask.myweatherapplication.data.entities.weather.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainWeatherData(
	val temp: Double,
	@SerialName("feels_like") val feelsLike: Double,
	@SerialName("temp_min") val tempMin: Double,
	@SerialName("temp_max") val tempMax: Double,
	val pressure: Int,
	val humidity: Int,
	@SerialName("sea_level") val seaLevel: Int? = null,
	@SerialName("grnd_level") val groundLevel: Int? = null
)
