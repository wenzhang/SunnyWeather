package com.wenzhang.sunnyweather.logic.network

import com.wenzhang.sunnyweather.SunnyWeatherApplication
import com.wenzhang.sunnyweather.logic.model.DailyResponse
import com.wenzhang.sunnyweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    /**
     * 获取实时天气信息
     * http://api.caiyunapp.com/v2.5/ZnmbTBqpHVrk1SF2/114.304569,30.593354/realtime.json
     */
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    /**
     * 获取未来几天天气信息
     */
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
}