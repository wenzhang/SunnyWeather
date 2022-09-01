package com.wenzhang.sunnyweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.wenzhang.sunnyweather.SunnyWeatherApplication
import com.wenzhang.sunnyweather.logic.model.Place

/**
 * 地区信息数据 DAO
 * 保存与读取选中地区信息 place json
 */
object PlaceDao {

    const val PLACE_SAVE_NAME = "sunny_weather"

    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSave() = sharedPreferences().contains("place")

    private fun sharedPreferences() = SunnyWeatherApplication.context.getSharedPreferences(
        PLACE_SAVE_NAME, Context.MODE_PRIVATE
    )
}