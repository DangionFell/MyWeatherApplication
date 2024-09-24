package com.testtask.myweatherapplication.data.local

import android.content.Context
import com.testtask.myweatherapplication.domain.model.other.Location

class AppStorageImpl(
	private val context: Context,
): AppStorage {
	companion object {
		const val LAST_LOCATION = "LAST_LOCATION"
		const val PREFS_NAME = "AppPrefs"
		const val LATITUDE_KEY = "LATITUDE"
		const val LONGITUDE_KEY = "LONGITUDE"
	}

	private val appPrefs by lazy {
		context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
	}

	override fun getLastLocation(): Location? {
		val locationSet = appPrefs.getStringSet(LAST_LOCATION, null)

		if (locationSet != null) {
			val locationMap = locationSet.associate {
				val (key, value) = it.split(":")
				key to value
			}

			val latitude = locationMap[LATITUDE_KEY]?.toDoubleOrNull()
			val longitude = locationMap[LONGITUDE_KEY]?.toDoubleOrNull()

			if (latitude != null && longitude != null) {
				return Location(latitude, longitude)
			}
		}
		return null
	}

	override fun saveLastLocation(location: Location) {
		val locationSet = setOf(
			"$LATITUDE_KEY:${location.latitude}",
			"$LONGITUDE_KEY:${location.longitude}"
		)
		appPrefs.edit()
			.putStringSet(LAST_LOCATION, locationSet)
			.apply()
	}
}