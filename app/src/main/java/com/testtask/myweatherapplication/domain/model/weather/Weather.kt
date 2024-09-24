package com.testtask.myweatherapplication.domain.model.weather

import com.testtask.myweatherapplication.data.entities.weather.model.Clouds
import com.testtask.myweatherapplication.data.entities.weather.model.MainWeatherData
import com.testtask.myweatherapplication.data.entities.weather.model.WeatherCondition
import com.testtask.myweatherapplication.data.entities.weather.model.Wind

data class Weather(
	val weather: List<WeatherCondition>,
	val main: MainWeatherData,
	val wind: Wind,
	val clouds: Clouds,
	val name: String,
)
