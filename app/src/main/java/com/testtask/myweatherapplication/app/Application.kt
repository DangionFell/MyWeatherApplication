package com.testtask.myweatherapplication.app

import android.app.Application
import org.koin.core.context.startKoin

class Application : Application()  {
	override fun onCreate() {
		super.onCreate()
		startKoin {

		}
	}
}