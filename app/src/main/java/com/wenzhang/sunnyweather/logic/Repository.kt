package com.wenzhang.sunnyweather.logic

import androidx.lifecycle.liveData
import com.wenzhang.sunnyweather.logic.dao.PlaceDao
import com.wenzhang.sunnyweather.logic.model.Place
import com.wenzhang.sunnyweather.logic.model.Weather
import com.wenzhang.sunnyweather.logic.network.SunnyWeatherNetwork
import com.wenzhang.sunnyweather.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层统一封装入口
 */
object Repository {

    /**
     * 根据关键词->搜索城市列表 reult->LiveDataScope<Result<List<Place>>>
     */
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)

        LogUtils.d(Repository.javaClass.simpleName, placeResponse.toString())

        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    /**
     * 根据经纬度,刷新天气信息
     */
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            LogUtils.d("Repository", realtimeResponse.toString())
            val dailyResponse = deferredDaily.await()
            LogUtils.d("Repository", dailyResponse.toString())
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(RuntimeException("realtime response status is ${realtimeResponse.status}" + "daily response status is ${dailyResponse.status}"))
            }
        }
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavePlace() = PlaceDao.getPlace()

    fun isPlaceSave() = PlaceDao.isPlaceSave()

    /**
     * 简化try-catch，统一异常处理
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>> {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}