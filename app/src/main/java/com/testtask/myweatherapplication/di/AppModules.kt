package com.testtask.myweatherapplication.di

import com.testtask.myweatherapplication.BuildConfig
import com.testtask.myweatherapplication.data.entities.weather.WeatherService
import com.testtask.myweatherapplication.data.entities.weather.WeatherServiceImpl
import com.testtask.myweatherapplication.data.local.AppStorage
import com.testtask.myweatherapplication.data.local.AppStorageImpl
import com.testtask.myweatherapplication.data.network.api.ApiClient
import com.testtask.myweatherapplication.data.network.api.ApiClientImpl
import com.testtask.myweatherapplication.presentation.screens.weather.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule =
	module {
		single<ApiClient> { ApiClientImpl(BuildConfig.SERVER_URL, BuildConfig.WEATHER_API_KEY) }
		single<AppStorage> { AppStorageImpl(get()) }
		single<WeatherService> { WeatherServiceImpl(get()) }
	}

val presentationModule =
	module {
		viewModel { WeatherViewModel(get(), get(), get()) }
	}

fun getAllModules() =
	listOf(
		dataModule,
		presentationModule
	)