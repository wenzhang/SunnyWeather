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

    private val placeService = ServiceCreator.create<PlaceService>()

    /**
     * 请求HTTP，访问城市列表Json->PlaceResponse
     */
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

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