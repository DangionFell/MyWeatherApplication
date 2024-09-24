package com.testtask.myweatherapplication.app

import android.app.Application
import android.content.Context
import com.testtask.myweatherapplication.di.getAllModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : Application()  {
	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@Application)

			modules(getAllModules())
		}
	}
}