package com.hye.healthpossible

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HealthpossibleApplication : Application(), SingletonImageLoader.Factory{

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        super.onCreate()
        healthpossibleApplication = this
        ToastHelper.init(this)
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return imageLoader
    }

    companion object{
        private lateinit var healthpossibleApplication: HealthpossibleApplication
        fun getAppContext() = healthpossibleApplication
    }
}