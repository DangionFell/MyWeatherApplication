package com.testtask.myweatherapplication.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class ApiClientImpl(
	private val baseUrl: String
) : ApiClient {

	private val client = HttpClient {
		install(ContentNegotiation) {
			json (
				Json {
					ignoreUnknownKeys = true
					isLenient = true
					prettyPrint = true
					useArrayPolymorphism = true
				}
			)
		}
	}

	override suspend fun <T : Any> get(
		relativePath: String,
		queryParams: Map<String, Any>?,
		headers: Map<String, String>?,
		typeInfo: TypeInfo
	): T {
		return sendRequest(
			HttpMethod.Get,
			"$baseUrl$relativePath",
			queryParams,
			null,
			headers,
			typeInfo
		)
	}

	override suspend fun <T : Any> post(
		relativePath: String,
		queryParams: Map<String, Any>?,
		requestBody: Any?,
		headers: Map<String, String>?,
		typeInfo: TypeInfo
	): T {
		return sendRequest(
			HttpMethod.Post,
			"$baseUrl$relativePath",
			queryParams,
			requestBody,
			headers,
			typeInfo
		)
	}

	private suspend fun <T> sendRequest(
		method: HttpMethod,
		endpoint: String,
		queryParams: Map<String, Any>? = null,
		body: Any? = null,
		headers: Map<String, String>? = null,
		typeInfo: TypeInfo
	): T = withContext(Dispatchers.IO) {
		val response: HttpResponse = client.request(endpoint) {
			this.method = method
			queryParams?.forEach { parameter(it.key, it.value) }
			headers?.forEach { header(it.key, it.value) }
			body?.let {
				contentType(ContentType.Application.Json)
				setBody(it)
			}
		}
		if (response.status == HttpStatusCode.OK) {
			response.body(typeInfo) as T
		} else {
			throw Exception("Request failed with status: ${response.status}")
		}
	}
}