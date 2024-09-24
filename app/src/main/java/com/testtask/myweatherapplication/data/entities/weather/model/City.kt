package com.testtask.myweatherapplication.data.entities.weather.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
	val id: Long,
	val name: String,
	val coord: Coord,
	val country: String,
	val population: Int,
	val timezone: Int,
	val sunrise: Long,
	val sunset: Long
)
