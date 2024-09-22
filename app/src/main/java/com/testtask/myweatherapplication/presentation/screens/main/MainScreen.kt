package com.testtask.myweatherapplication.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey

class MainScreen : Screen {
	override val key: ScreenKey = uniqueScreenKey

	@Composable
	override fun Content() {
		Box(modifier = Modifier.background(Color.Red))
	}
}