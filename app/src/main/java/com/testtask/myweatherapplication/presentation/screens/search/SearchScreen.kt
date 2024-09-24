package com.testtask.myweatherapplication.presentation.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.testtask.myweatherapplication.R
import com.testtask.myweatherapplication.domain.model.city.City
import com.testtask.myweatherapplication.domain.model.other.Location
import com.testtask.myweatherapplication.presentation.screens.weather.WeatherScreen
import com.testtask.myweatherapplication.presentation.screens.weather.model.WeatherScreenParams
import com.testtask.myweatherapplication.ui.theme.Colors

class SearchScreen: Screen {
	override val key: ScreenKey = uniqueScreenKey

	@Composable
	override fun Content() {
		SearchScreenContent()
	}
}

@Composable
private fun SearchScreenContent() {
	val navigator = LocalNavigator.currentOrThrow

	var query by remember { mutableStateOf(TextFieldValue("")) }

	val cities = listOf(
		City("Москва", 55.7558, 37.6173),
		City("Санкт-Петербург", 59.9311, 30.3609),
		City("Новосибирск", 55.0084, 82.9357),
		City("Екатеринбург", 56.8389, 60.6057),
		City("Казань", 55.7963, 49.1088),
		City("Нижний Новгород", 56.2965, 43.9361),
		City("Челябинск", 55.1644, 61.4368),
		City("Самара", 53.2415, 50.2212),
		City("Омск", 54.9893, 73.3682),
		City("Ростов-на-Дону", 47.2362, 39.7129)
	)

	val filteredCities = cities.filter { it.name.contains(query.text, ignoreCase = true) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		Spacer(Modifier.height(32.dp))

		OutlinedTextField(
			value = query,
			onValueChange = { query = it },
			label = { Text(stringResource(R.string.common_search)) },
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 16.dp)
		)

		LazyColumn {
			items(filteredCities) { city ->
				CityItem(city = city.name) {
					navigator.replaceAll(
						WeatherScreen(
							WeatherScreenParams(
								cityName = city.name,
								location = Location(city.latitude, city.longitude)
							)
						)
					)
				}
			}
		}
	}
}

@Composable
fun CityItem(city: String, onClick: () -> Unit) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp)
			.clickable(onClick = onClick),
		colors = CardDefaults.cardColors(containerColor = Colors.White)
	) {
		Text(
			text = city,
			modifier = Modifier
				.padding(16.dp),
			style = MaterialTheme.typography.bodyLarge
		)
	}
}