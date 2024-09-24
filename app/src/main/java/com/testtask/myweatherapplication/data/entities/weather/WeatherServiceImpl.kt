package com.testtask.myweatherapplication.data.entities.weather

import com.testtask.myweatherapplication.data.entities.weather.mapper.toDomainModel
import com.testtask.myweatherapplication.data.entities.weather.model.ForecastResponse
import com.testtask.myweatherapplication.data.entities.weather.model.WeatherResponse
import com.testtask.myweatherapplication.data.network.api.ApiClient
import com.testtask.myweatherapplication.domain.model.other.Location
import com.testtask.myweatherapplication.domain.model.weather.Forecast
import com.testtask.myweatherapplication.domain.model.weather.Weather
import io.ktor.util.reflect.typeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherServiceImpl(
	private val apiClient: ApiClient
) : WeatherService {

	override fun getWeather(location: Location): Flow<Weather> = flow {
		val queryParams = mapOf(
			"lat" to location.latitude,
			"lon" to location.longitude,
			"units" to "metric",
			"lang" to "ru"
		)

		val weatherResponse: WeatherResponse = apiClient.get(
			relativePath = "weather",
			queryParams = queryParams,
			headers = null,
			typeInfo = typeInfo<WeatherResponse>()
		)

		val weather = weatherResponse.toDomainModel()
		emit(weather)
	}

	override fun geWeatherForecast(location: Location): Flow<Forecast> = flow {
		val queryParams = mapOf(
			"lat" to location.latitude,
			"lon" to location.longitude,
			"units" to "metric",
			"lang" to "ru"
		)

		val forecastResponse: ForecastResponse = apiClient.get(
			relativePath = "forecast",
			queryParams = queryParams,
			headers = null,
			typeInfo = typeInfo<ForecastResponse>()
		)

		emit(forecastResponse.toDomainModel())
	}
}