package com.wenzhang.sunnyweather.logic.model

/**
 * 实时天气和未来几天天气Model
 */
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)
