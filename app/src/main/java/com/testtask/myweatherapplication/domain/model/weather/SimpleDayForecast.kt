package com.testtask.myweatherapplication.domain.model.weather

data class SimpleDayForecast(
	val date: String,
	val averageTemp: Int,
	val minTemp: Int,
	val maxTemp: Int,
	val description: String
)
