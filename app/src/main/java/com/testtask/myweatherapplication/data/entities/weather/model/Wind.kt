package com.testtask.myweatherapplication.data.entities.weather.model

import kotlinx.serialization.Serializable

@Serializable
data class Wind(
	val speed: Double,
	val deg: Int,
	val gust: Double? = null
)

