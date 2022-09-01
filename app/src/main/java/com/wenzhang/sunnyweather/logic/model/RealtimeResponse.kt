package com.wenzhang.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 获取实时天气信息 model
 * {"status":"ok","api_version":"v2.5","api_status":"active","lang":"zh_CN","unit":"metric","tzshift":28800,"timezone":"Asia/Shanghai","server_time":1661997478,"location":[30.593354,114.304569],"result":{"realtime":{"status":"ok","temperature":23,"humidity":0.76,"cloudrate":1,"skycon":"CLOUDY","visibility":29,"dswrf":66.1,"wind":{"speed":4,"direction":39},"pressure":101269.06,"apparent_temperature":24.8,"precipitation":{"local":{"status":"ok","datasource":"radar","intensity":0},"nearest":{"status":"ok","distance":73.49,"intensity":0.1875}},"air_quality":{"pm25":8,"pm10":11,"o3":39,"so2":16,"no2":33,"co":1.5,"aqi":{"chn":17,"usa":33},"description":{"chn":"优","usa":"优"}},"life_index":{"ultraviolet":{"index":2,"desc":"很弱"},"comfort":{"index":4,"desc":"温暖"}}},"primary":0}}
 */
data class RealtimeResponse(val status: String, val result: Result) {

    data class Result(val realtime: Realtime)

    data class Realtime(
        val skycon: String,
        val temperature: Float,
        @SerializedName("air_quality") val airQuality: AirQuality
    )

    data class AirQuality(val aqi: Aqi)

    data class Aqi(val chn: Float)
}