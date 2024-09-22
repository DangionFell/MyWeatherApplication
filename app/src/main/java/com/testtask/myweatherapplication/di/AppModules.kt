package com.testtask.myweatherapplication.di

import com.testtask.myweatherapplication.BuildConfig
import com.testtask.myweatherapplication.data.network.api.ApiClient
import com.testtask.myweatherapplication.data.network.api.ApiClientImpl
import org.koin.dsl.module

val dataModule =
	module {
		single<ApiClient> { ApiClientImpl(BuildConfig.SERVER_URL) }
	}