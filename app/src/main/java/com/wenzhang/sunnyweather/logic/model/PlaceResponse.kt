package com.wenzhang.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName


/**
 * 获取地区天气信息 model
 *
 * URL -> https://api.caiyunapp.com/v2/place?query=%E5%8C%97%E4%BA%AC&token=ZnmbTBqpHVrk1SF2&lang=zh_CN
 *
 * result ->
 * {"status":"ok","query":"北京",
 *  "places":[
 *      {"id":"B000A83M61","name":"北京西站","formatted_address":"中国 北京市 丰台区 莲花池东路118号","location":{"lat":39.89491,"lng":116.322056},"place_id":"a-B000A83M61"},
 *      {"id":"B000A83AJN","name":"北京南站","formatted_address":"中国 北京市 丰台区 永外大街车站路12号","location":{"lat":39.865246,"lng":116.378517},"place_id":"a-B000A83AJN"},
 *      {"id":"BV11317432","name":"北京东站(地铁站)","formatted_address":"中国 北京市 朝阳区 (在建)28号线","location":{"lat":39.902267,"lng":116.482682},"place_id":"a-BV11317432"},
 *      {"id":"B000A833V8","name":"北京北站","formatted_address":"中国 北京市 西城区 北滨河路1号","location":{"lat":39.944876,"lng":116.353063},"place_id":"a-B000A833V8"},
 *      {"id":"B000A83C36","name":"北京站","formatted_address":"中国 北京市 东城区 毛家湾胡同甲13号","location":{"lat":39.902842,"lng":116.427341},"place_id":"a-B000A83C36"}
 * ]}
 */

data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String,
    val location: Location,
    @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)
