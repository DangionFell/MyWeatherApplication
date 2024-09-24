package com.testtask.myweatherapplication.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.testtask.myweatherapplication.presentation.screens.main.MainScreen
import com.testtask.myweatherapplication.presentation.screens.weather.WeatherScreen
import com.testtask.myweatherapplication.ui.theme.MyWeatherApplicationTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			MyWeatherApplicationTheme {
				Navigator(WeatherScreen())
			}
		}
	}
}