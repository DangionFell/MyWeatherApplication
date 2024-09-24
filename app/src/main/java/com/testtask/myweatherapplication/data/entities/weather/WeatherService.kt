package com.testtask.myweatherapplication.data.entities.weather

import com.testtask.myweatherapplication.domain.model.other.Location
import com.testtask.myweatherapplication.domain.model.weather.Forecast
import com.testtask.myweatherapplication.domain.model.weather.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherService {
	fun getWeather(location: Location): Flow<Weather>

	fun geWeatherForecast(location: Location): Flow<Forecast>
}