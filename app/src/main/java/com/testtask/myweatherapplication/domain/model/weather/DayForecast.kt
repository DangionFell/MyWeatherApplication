package com.testtask.myweatherapplication.domain.model.weather

data class DayForecast(
	val date: String,
	val averageTemp: Int,
	val minTemp: Int,
	val maxTemp: Int,
	val description: String
)
