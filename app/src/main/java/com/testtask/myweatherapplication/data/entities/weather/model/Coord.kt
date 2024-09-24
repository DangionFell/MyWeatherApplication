package com.testtask.myweatherapplication.data.entities.weather.model

import kotlinx.serialization.Serializable

@Serializable
data class Coord(
	val lon: Double,
	val lat: Double
)
