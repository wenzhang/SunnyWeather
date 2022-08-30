package com.wenzhang.sunnyweather.logic.network

import com.wenzhang.sunnyweather.SunnyWeatherApplication
import com.wenzhang.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    /**
     * 获取城市列表
     * params query: 城市名称
     * result: PlaceResponse
     */
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}