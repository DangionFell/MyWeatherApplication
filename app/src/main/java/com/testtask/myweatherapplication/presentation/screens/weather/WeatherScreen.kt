package com.testtask.myweatherapplication.presentation.screens.weather

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.testtask.myweatherapplication.R
import com.testtask.myweatherapplication.data.entities.weather.model.WeatherForecast
import com.testtask.myweatherapplication.domain.model.weather.SimpleDayForecast
import com.testtask.myweatherapplication.domain.model.weather.Weather
import com.testtask.myweatherapplication.presentation.screens.search.SearchScreen
import com.testtask.myweatherapplication.presentation.screens.weather.model.WeatherScreenParams
import com.testtask.myweatherapplication.ui.theme.Typography
import com.testtask.myweatherapplication.ui.theme.Colors
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

class WeatherScreen(
	private val params: WeatherScreenParams? = null
) : Screen {
	override val key: ScreenKey = uniqueScreenKey

	private val permissionsToRequest = arrayOf(
		android.Manifest.permission.ACCESS_COARSE_LOCATION,
		android.Manifest.permission.ACCESS_FINE_LOCATION,
	)

	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		val viewModel: WeatherViewModel = koinViewModel()
		val uiState = viewModel.state.collectAsState()
		var isPermissionGranted = false

		val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
			contract = ActivityResultContracts.RequestMultiplePermissions(),
			onResult = { perms ->
				isPermissionGranted = perms[permissionsToRequest.first()] == true ||
						perms[permissionsToRequest.last()] == true
				viewModel.onEvent(
					WeatherEvent.ResolveWeatherSource(
						isPermissionGranted
					)
				)
			}
		)

		LaunchedEffect(Unit) {
			if (params != null) {
				viewModel.onEvent(
					WeatherEvent.GetWeatherForecast(
						params.location
					)
				)
			} else {
				multiplePermissionResultLauncher.launch(
					permissionsToRequest
				)
			}
		}

		val pullRefreshState =
			rememberPullRefreshState(
				refreshing = uiState.value.isLoading,
				onRefresh = {
					if (params != null) {
						viewModel.onEvent(
							WeatherEvent.GetWeatherForecast(
								params.location
							)
						)
					} else {
						viewModel.onEvent(
							WeatherEvent.ResolveWeatherSource(
								isPermissionGranted
							)
						)
					}
				},
			)

		Box(
			modifier =
			Modifier
				.pullRefresh(pullRefreshState)
				.fillMaxSize(),
		) {
			when {
				uiState.value.isLoading -> {
					LoaderScreen()
				}
				uiState.value.weather != null -> {
					uiState.value.weather?.let { weather ->
						WeatherScreenContent(weather, uiState)
					}
				}
				uiState.value.errorMessage != null -> {
					uiState.value.errorMessage?.let {
						ErrorPopUp(
							text = it,
							modifier = Modifier
								.padding(
									top = 24.dp,
									start = 24.dp,
									end = 24.dp
								),
							onDismiss = {
								viewModel.onEvent(WeatherEvent.OnDismissError)
							}
						)
					}
				}
			}
			PullRefreshIndicator(
				refreshing = uiState.value.isLoading,
				state = pullRefreshState,
				modifier = Modifier.align(Alignment.TopCenter),
			)
		}
	}
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun WeatherScreenContent(
	weather: Weather,
	uiState: State<WeatherState>
) {
	val navigator = LocalNavigator.currentOrThrow

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = weather.name,
						modifier = Modifier
							.fillMaxWidth()
							.padding(start = 24.dp),
						style = Typography.headlineMedium.copy(
							color = Color.White,
							fontWeight = FontWeight.SemiBold,
							textAlign = TextAlign.Center
						)
					)
				},
				actions = {
					IconButton(
						onClick = {
							navigator.push(SearchScreen())
						}
					) {
						Icon(
							imageVector = Icons.Default.Search,
							contentDescription = null,
							modifier = Modifier.size(32.dp),
							tint = Color.White
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors().copy(
					containerColor = Colors.CobaltBlue76FF
				)
			)
		},
		containerColor = Colors.CobaltBlue76FF
	) { paddingValues ->
		uiState.value.simpleForecast?.let { simpleForecasts ->
			val expandedStates = rememberSaveable { mutableStateOf(setOf<String>()) }

			LazyColumn {
				item(key = "header") {
					WeatherScreenHeader(paddingValues, weather)
				}
				item(key = "todayForecast") {
					TodayForecast(uiState)
				}
				item(key = "forecastTitle") {
					Spacer(modifier = Modifier.height(16.dp))
					Text(
						text = stringResource(R.string.weather_forecast),
						modifier = Modifier.fillMaxWidth(),
						style = Typography.headlineMedium.copy(
							color = Color.White,
							fontWeight = FontWeight.SemiBold,
							textAlign = TextAlign.Center
						)
					)
					Spacer(modifier = Modifier.height(8.dp))
				}

				val detailedForecasts = uiState.value.detailedForecast
				simpleForecasts.forEachIndexed { index, simpleDayForecast ->
					item(key = simpleDayForecast.date) {
						val isExpanded = expandedStates.value.contains(simpleDayForecast.date)
						WeatherCardWithDate(
							modifier = Modifier.padding(horizontal = 16.dp),
							simpleDayForecast = simpleDayForecast,
							detailedForecast = detailedForecasts?.get(index) ?: emptyList(),
							isExpanded = isExpanded,
							onClick = {
								expandedStates.value = if (isExpanded) {
									expandedStates.value - simpleDayForecast.date
								} else {
									expandedStates.value + simpleDayForecast.date
								}
							}
						)
						Spacer(Modifier.height(16.dp))
					}
				}
			}
		}
	}
}

@Composable
private fun TodayForecast(uiState: State<WeatherState>) {
	val detailedForecasts = uiState.value.detailedForecast
	val firstDayForecast = detailedForecasts?.firstOrNull() ?: emptyList()
	LazyRow(
		modifier = Modifier
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		item { Spacer(modifier = Modifier.width(8.dp)) }
		items(firstDayForecast, key = { it.dateTimeText }) { forecast ->
			WeatherCard(
				time = forecast.dateTimeText.split(" ")[1].substring(0, 5),
				temperature = forecast.main.temp.toInt(),
				description = forecast.weather.firstOrNull()?.description
					?: stringResource(R.string.common_unknown)
			)
		}
		item { Spacer(modifier = Modifier.width(8.dp)) }
	}
}

@Composable
private fun WeatherScreenHeader(
	paddingValues: PaddingValues,
	weather: Weather
) {
	Spacer(Modifier.height(paddingValues.calculateTopPadding() + 32.dp))
	Text(
		text = stringResource(
			R.string.temperature_in_celsius,
			weather.main.temp.toInt().toString()
		),
		modifier = Modifier.fillMaxWidth(),
		style = TextStyle(
			color = Color.White,
			fontSize = 52.sp,
			fontWeight = FontWeight.SemiBold,
			textAlign = TextAlign.Center
		)
	)
	Spacer(modifier = Modifier.height(16.dp))
	Text(
		text = stringResource(
			R.string.weather_description_with_min_and_max_temperature,
			weather.weather.first().description,
			weather.main.tempMin.toInt().toString(),
			weather.main.tempMax.toInt().toString()
		),
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp),
		style = Typography.titleLarge.copy(
			color = Color.White,
			fontWeight = FontWeight.SemiBold,
			textAlign = TextAlign.Center
		)
	)
	Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun LoaderScreen() {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(Colors.CobaltBlue76FF)
	) {
		CircularProgressIndicator(
			modifier = Modifier
				.align(Alignment.Center),
			color = Color.White
		)
	}
}

@Composable
private fun WeatherCard(
	time: String,
	temperature: Int,
	description: String
) {
	Card(
		modifier = Modifier
			.fillMaxWidth(),
		colors = CardDefaults.cardColors().copy(
			containerColor = Color.White
		),
		shape = RoundedCornerShape(16.dp),
	) {
		Column(
			modifier = Modifier
				.padding(16.dp)
				.width(120.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.Start
		) {
			Text(
				text = time,
				fontSize = 18.sp,
				fontWeight = FontWeight.SemiBold,
				color = Colors.Black0B
			)
			Text(
				text = stringResource(R.string.temperature_in_celsius, temperature),
				fontSize = 16.sp,
				fontWeight = FontWeight.SemiBold,
				color = Colors.CobaltBlue76FF
			)
			Text(
				text = description,
				fontSize = 16.sp,
				fontWeight = FontWeight.SemiBold,
				color = Colors.Red66,
				minLines = 2,
				maxLines = 2
			)
		}
	}
}

@Composable
private fun WeatherCardWithDate(
	modifier: Modifier,
	simpleDayForecast: SimpleDayForecast,
	detailedForecast: List<WeatherForecast>,
	isExpanded: Boolean,
	onClick: () -> Unit
) {
	Card(
		modifier = modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(16.dp)),
		colors = CardDefaults.cardColors().copy(
			containerColor = Color.White
		),
		shape = RoundedCornerShape(16.dp),
		onClick = onClick
	) {
		Row(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = simpleDayForecast.date,
				fontSize = 16.sp,
				fontWeight = FontWeight.SemiBold,
				color = Colors.Black0B
			)
			Spacer(modifier = Modifier.width(8.dp))
			Column {
				Text(
					text = stringResource(R.string.temperature_in_celsius, simpleDayForecast.averageTemp),
					modifier = Modifier.align(Alignment.CenterHorizontally),
					fontSize = 20.sp,
					fontWeight = FontWeight.SemiBold,
					textAlign = TextAlign.Center,
					color = Colors.CobaltBlue76FF
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = simpleDayForecast.description,
					fontSize = 16.sp,
					color = Colors.CobaltBlue76FF
				)
			}
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = stringResource(
					R.string.min_and_max_temperature,
					simpleDayForecast.minTemp,
					simpleDayForecast.maxTemp,
				),
				fontSize = 16.sp,
				fontWeight = FontWeight.SemiBold,
				color = Colors.Black0B,
			)
		}
		if (isExpanded) {
			Column {
				detailedForecast.forEach { forecast ->
					HorizontalDivider(Modifier.fillMaxWidth(), color = Colors.Black0B)
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(8.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						Text(
							text = forecast.dateTimeText.split(" ")[1].substring(0, 5),
							fontSize = 16.sp,
							fontWeight = FontWeight.SemiBold,
							color = Colors.Black0B
						)
						Spacer(Modifier.width(16.dp))
						Text(
							text = stringResource(
								R.string.temperature_in_celsius,
								forecast.main.temp.toInt()
							),
							fontSize = 16.sp,
							fontWeight = FontWeight.SemiBold,
							color = Colors.CobaltBlue76FF
						)
						Text(
							text = forecast.weather.firstOrNull()?.description
								?: stringResource(R.string.common_unknown),
							modifier = Modifier.fillMaxWidth(),
							textAlign = TextAlign.End,
							fontSize = 16.sp,
							fontWeight = FontWeight.SemiBold,
							color = Colors.CobaltBlue76FF
						)
					}
				}
			}
		}
	}
}

@Composable
fun ErrorPopUp(
	text: String,
	modifier: Modifier,
	onDismiss: () -> Unit
) {
	var isVisible by remember { mutableStateOf(true) }

	LaunchedEffect(key1 = isVisible) {
		if (!isVisible) {
			delay(1000)
			onDismiss()
		} else {
			delay(3000)
			isVisible = false
		}
	}

	AnimatedVisibility(
		visible = isVisible,
		exit = fadeOut(animationSpec = tween(durationMillis = 300)) + slideOutVertically(
			targetOffsetY = { -it }
		),
	) {
		Card(
			modifier = modifier,
			colors = CardDefaults.cardColors().copy(
				containerColor = Color.Red,
			),
			shape = RoundedCornerShape(8.dp)
		) {
			Row(
				modifier = modifier
					.fillMaxWidth()
					.padding(
						vertical = 12.dp,
						horizontal = 16.dp
					),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Icon(
					imageVector = Icons.Default.Info,
					text,
					tint = Color.White
				)
				Spacer(modifier = Modifier.width(16.dp))
				Text(
					text = text,
					fontSize = 12.sp,
					fontWeight = FontWeight.SemiBold,
					color = Colors.White
				)
			}
		}
	}
}