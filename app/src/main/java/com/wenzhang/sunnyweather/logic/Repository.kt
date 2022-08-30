package com.wenzhang.sunnyweather.logic

import androidx.lifecycle.liveData
import com.wenzhang.sunnyweather.logic.network.SunnyWeatherNetwork
import com.wenzhang.sunnyweather.util.LogUtils
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

/**
 * 仓库层统一封装入口
 */
object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            LogUtils.d(Repository.javaClass.simpleName, placeResponse.toString())
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
}