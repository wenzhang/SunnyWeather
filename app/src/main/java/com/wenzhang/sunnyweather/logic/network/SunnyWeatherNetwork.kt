package com.wenzhang.sunnyweather.logic.network

import com.wenzhang.sunnyweather.util.LogUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 统一网络数据源访问入口
 */
object SunnyWeatherNetwork {

    //placeService
    private val placeService = ServiceCreator.create<PlaceService>()

    //weatherService
    private val weatherService = ServiceCreator.create<WeatherService>()

    /**
     * 请求HTTP，访问城市列表Json->PlaceResponse
     */
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    /**
     * 请求HTTP 访问未来几天天气信息Json->DailyResponse
     */
    suspend fun getDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    /**
     * 请求HTTP 访问实时天气信息Json->RealtimeResponse
     */
    suspend fun getRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

    /**
     * Call 回调函数封装
     * success or failure 返回数据
     * suspendCoroutine 挂起当前函数，开启子线程执行Lamda内容，请求完毕唤醒函数并返回数据
     */
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine {
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        it.resume(body)
                    } else it.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }
            })
        }
    }
}