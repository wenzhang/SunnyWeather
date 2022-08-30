package com.wenzhang.sunnyweather.util

import android.widget.Toast
import com.wenzhang.sunnyweather.SunnyWeatherApplication

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(SunnyWeatherApplication.context, this, duration).show()
}