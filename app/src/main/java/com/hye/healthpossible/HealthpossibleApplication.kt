package com.hye.healthpossible

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HealthpossibleApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        healthpossibleApplication = this
        ToastHelper.init(this)
    }
    companion object{
        private lateinit var healthpossibleApplication: HealthpossibleApplication
        fun getAppContext() = healthpossibleApplication
    }
}