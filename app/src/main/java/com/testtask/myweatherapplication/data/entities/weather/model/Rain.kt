package com.testtask.myweatherapplication.data.entities.weather.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rain(
	@SerialName("3h") val threeHour: Double? = null
)
