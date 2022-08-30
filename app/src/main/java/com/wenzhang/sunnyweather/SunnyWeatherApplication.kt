package com.wenzhang.sunnyweather

import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {

    companion object {
        lateinit var context: Context

        // 彩云API令牌
        const val TOKEN = "ZnmbTBqpHVrk1SF2"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}