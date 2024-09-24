package com.testtask.myweatherapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
	headlineSmall = TextStyle(
		fontSize = 24.sp,
		lineHeight = 32.sp,
	),

	titleLarge = TextStyle(
		fontSize = 20.sp,
		lineHeight = 28.sp,
	),

	titleMedium = TextStyle(
		fontSize = 18.sp,
		lineHeight = 28.sp,
	),

	titleSmall = TextStyle(
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.15.sp
	),

	bodyLarge = TextStyle(
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.5.sp
	),

	bodyMedium = TextStyle(
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.25.sp
	),

	bodySmall = TextStyle(
		fontSize = 12.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.4.sp
	),

	labelLarge = TextStyle(
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.15.sp,
	),

	labelMedium = TextStyle(
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.1.sp,
	),

	labelSmall = TextStyle(
		fontSize = 12.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.5.sp,
	)
)