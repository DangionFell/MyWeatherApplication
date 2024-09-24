package com.testtask.myweatherapplication.presentation.screens.main

import android.app.Activity
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task

class MainScreen : Screen {
	override val key: ScreenKey = uniqueScreenKey

	lateinit var fusedLocationClient: Task<Location>

	private val permissionsToRequest = arrayOf(
		android.Manifest.permission.ACCESS_COARSE_LOCATION,
		android.Manifest.permission.ACCESS_FINE_LOCATION,
	)

	@Composable
	override fun Content() {
		val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
			contract = ActivityResultContracts.RequestMultiplePermissions(),
			onResult = { perms ->
				println(perms[permissionsToRequest[0]] == true || perms[permissionsToRequest[1]] == true)
			}
		)

		LaunchedEffect(Unit) {
			multiplePermissionResultLauncher.launch(
				permissionsToRequest
			)
		}

		var loc by remember { mutableStateOf("fafa") }

		Box(modifier = Modifier
			.background(Color.Red)
			.fillMaxSize()) {
			Text(text = loc)
		}
	}
}