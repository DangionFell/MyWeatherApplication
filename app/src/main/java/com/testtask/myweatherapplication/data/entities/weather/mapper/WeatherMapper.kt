package com.testtask.myweatherapplication.data.entities.weather.mapper

import com.testtask.myweatherapplication.data.entities.weather.model.ForecastResponse
import com.testtask.myweatherapplication.data.entities.weather.model.WeatherResponse
import com.testtask.myweatherapplication.domain.model.weather.Forecast
import com.testtask.myweatherapplication.domain.model.weather.Weather

fun WeatherResponse.toDomainModel(): Weather {
	return Weather(
		weather = weather,
		main = main,
		wind = wind,
		clouds = clouds,
		name = name,
	)
}

fun ForecastResponse.toDomainModel(): Forecast {
	return Forecast(
		list = list,
	)
}