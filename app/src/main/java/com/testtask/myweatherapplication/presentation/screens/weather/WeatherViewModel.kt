package com.testtask.myweatherapplication.presentation.screens.weather

import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testtask.myweatherapplication.data.entities.weather.WeatherService
import com.testtask.myweatherapplication.data.entities.weather.model.WeatherForecast
import com.testtask.myweatherapplication.data.local.AppStorage
import com.testtask.myweatherapplication.domain.model.other.Location
import com.testtask.myweatherapplication.domain.model.weather.SimpleDayForecast
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(
	private val appStorage: AppStorage,
	private val weatherService: WeatherService,
	private val context: Context,
): ViewModel() {
	companion object {
		private val defaultLocation = Location(55.751244, 37.618423)
	}

	private val _state = MutableStateFlow(WeatherState())

	val state = _state.asStateFlow()

	fun onEvent(event: WeatherEvent) = when(event) {
		is WeatherEvent.ResolveWeatherSource -> resolveWeatherSource(event.isPermissionGranted)
		is WeatherEvent.GetWeatherForecast -> getSimpleWeatherInfo(event.location)
		is WeatherEvent.OnDismissError -> clearError()
	}

	private fun resolveWeatherSource(isPermissionGranted: Boolean) {
		viewModelScope.launch {
			val lastLocation = appStorage.getLastLocation()

			if (isPermissionGranted) {
				val location = getCurrentLocation()
				appStorage.saveLastLocation(location)
				getSimpleWeatherInfo(location)
			} else if (lastLocation != null) {
				getSimpleWeatherInfo(lastLocation)
			} else {
				getSimpleWeatherInfo(defaultLocation)
			}
		}
	}

	private fun getCurrentLocation(): Location {
		val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

		val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
		val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

		if (!isGpsEnabled && !isNetworkEnabled) {
			return defaultLocation
		}

		try {
			val provider = if (isGpsEnabled) {
				LocationManager.GPS_PROVIDER
			} else {
				LocationManager.NETWORK_PROVIDER
			}

			val androidLocation = locationManager.getLastKnownLocation(provider)

			return androidLocation?.let {
				Location(it.latitude, it.longitude)
			} ?: defaultLocation
		} catch (e: SecurityException) {
			return defaultLocation
		}
	}

	private fun getSimpleWeatherInfo(location: Location) {
		weatherService.getWeather(location).runOnBackground(
			startAction = {
				_state.update {
					it.copy(isLoading = true)
				}
			},
			onSuccess = { weather ->
				_state.update {
					it.copy(
						weather = weather,
						isLoading = false
					)
				}
				getWeatherForecast(location)
			},
			onFailure = { throwable ->
				Log.w("Error", throwable)
				_state.update {
					it.copy(
						errorMessage = "Что-то пошло не так, попробуйте позже, возможно стоит включить VPN",
						isLoading = false
					)
				}
			}
		)
	}

	private fun getWeatherForecast(location: Location) {
		weatherService.geWeatherForecast(location).runOnBackground(
			startAction = {
				_state.update {
					it.copy(isLoading = true)
				}
			},
			onSuccess = { forecast ->
				val groupedForecasts = groupForecastsByDay(forecast.list)
				val simpleForecast = groupedForecasts.map { (date, dayForecasts) ->
					val averageTemp = dayForecasts.map { it.main.temp }.average().toInt()
					val minTemp = dayForecasts.minOf { it.main.tempMin }.toInt()
					val maxTemp = dayForecasts.maxOf { it.main.tempMax }.toInt()
					val description = dayForecasts.firstOrNull()?.weather?.firstOrNull()?.description ?: "Неизвестно"

					SimpleDayForecast(
						date = formatDateToMMDD(date),
						averageTemp = averageTemp,
						minTemp = minTemp,
						maxTemp = maxTemp,
						description = description
					)
				}

				val detailedForecast = groupedForecasts.values.toList()

				_state.update {
					it.copy(
						simpleForecast = simpleForecast.take(5),
						detailedForecast = detailedForecast.take(5),
						isLoading = false
					)
				}
			},
			onFailure = { throwable ->
				Log.w("Error", throwable)
				_state.update {
					it.copy(
						errorMessage = "Что-то пошло не так, попробуйте позже, возможно стоит включить VPN",
						isLoading = false
					)
				}
			}
		)
	}

	private fun groupForecastsByDay(forecasts: List<WeatherForecast>): Map<String, List<WeatherForecast>> {
		return forecasts.groupBy { forecast ->
			val dateTimeParts = forecast.dateTimeText.split(" ")
			val forecastDate = dateTimeParts[0]
			val forecastTime = dateTimeParts[1]

			if (forecastTime == "00:00:00") {
				LocalDate.parse(forecastDate).minusDays(1).toString()
			} else {
				forecastDate
			}
		}
	}

	private fun formatDateToMMDD(dateTimeText: String): String {
		val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
		val outputFormat = SimpleDateFormat("MM/dd", Locale.getDefault())

		val date = inputFormat.parse(dateTimeText)

		return outputFormat.format(date ?: Date())
	}

	private fun clearError() {
		_state.update {
			it.copy(
				errorMessage = null
			)
		}
	}

	private fun <T : Any> Flow<T>.runOnBackground(
		startAction: () -> Unit = {},
		onSuccess: (T) -> Unit,
		onFailure: (Throwable) -> Unit
	): Job {
		return viewModelScope.launch {
			this@runOnBackground
				.onStart { startAction() }
				.catch { throwable ->
					onFailure(throwable)
				}
				.collect { value ->
					onSuccess(value)
				}
		}
	}
}