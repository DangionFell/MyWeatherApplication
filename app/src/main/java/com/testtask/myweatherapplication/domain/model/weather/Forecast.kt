package com.testtask.myweatherapplication.domain.model.weather

import com.testtask.myweatherapplication.data.entities.weather.model.WeatherForecast

data class Forecast(
	val list: List<WeatherForecast>,
)
