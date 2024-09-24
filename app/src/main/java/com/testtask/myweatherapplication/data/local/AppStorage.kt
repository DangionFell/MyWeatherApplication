package com.testtask.myweatherapplication.data.local

import com.testtask.myweatherapplication.domain.model.other.Location

interface AppStorage {

	fun getLastLocation(): Location?

	fun saveLastLocation(location: Location)
}