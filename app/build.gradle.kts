plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.kotlinx.serialization)
}

android {
	namespace = "com.testtask.myweatherapplication"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.testtask.myweatherapplication"
		minSdk = 28
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}

		buildConfigField("String", "SERVER_URL", "\"https://api.openweathermap.org/data/2.5/\"")
		buildConfigField("String", "WEATHER_API_KEY", "\"3524ed9a8f810627c4c706d5e02da730\"")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.1"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)

	// Compose
	implementation(libs.androidx.ui)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material)

	// For testing
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)

	// Koin DI
	implementation(platform(libs.koin.bom))
	implementation(libs.koin.core)
	implementation(libs.koin.android)
	implementation(libs.koin.androidx.compose)

	// Coroutines
	implementation(libs.coroutines.core)
	implementation(libs.coroutines.android)

	// for LocalDateTime support
	coreLibraryDesugaring(libs.desugar.jdk.libs)

	// Navigation + animations
	implementation(libs.voyager.navigator)
	implementation(libs.voyager.tab.navigator)
	implementation(libs.voyager.transitions)

	// Ktor
	implementation(libs.ktor.client.okhttp)
	implementation(libs.ktor.client.core)
	implementation(libs.ktor.client.contentNegotiation)
	implementation(libs.ktor.serialization.json)

	// Permission
	implementation(libs.accompanist.permissions)

	// Location
	implementation(libs.google.location)
}