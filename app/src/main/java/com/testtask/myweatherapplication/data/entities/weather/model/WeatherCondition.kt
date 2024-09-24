package com.testtask.myweatherapplication.data.entities.weather.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherCondition(
	val id: Int,
	val main: String,
	val description: String,
	val icon: String
)
