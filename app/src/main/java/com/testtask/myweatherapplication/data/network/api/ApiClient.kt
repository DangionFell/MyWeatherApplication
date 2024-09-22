package com.testtask.myweatherapplication.data.network.api

import io.ktor.util.reflect.TypeInfo

interface ApiClient {

	suspend fun <T : Any> get(
		relativePath: String,
		queryParams: Map<String, Any>? = null,
		headers: Map<String, String>? = null,
		typeInfo: TypeInfo
	): T

	suspend fun <T : Any> post(
		relativePath: String,
		queryParams: Map<String, Any>? = null,
		requestBody: Any? = null,
		headers: Map<String, String>? = null,
		typeInfo: TypeInfo
	): T
}