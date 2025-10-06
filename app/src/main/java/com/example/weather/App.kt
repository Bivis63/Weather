package com.example.weather

import android.app.Application
import com.example.weather.utils.AppDependencies

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        AppDependencies.initialize(this)
    }
}