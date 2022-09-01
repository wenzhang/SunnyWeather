package com.wenzhang.sunnyweather.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wenzhang.sunnyweather.logic.Repository
import com.wenzhang.sunnyweather.logic.dao.PlaceDao
import com.wenzhang.sunnyweather.logic.model.Place

class PlaceViewModel : ViewModel() {

    //switch observer
    private val searchLiveData = MutableLiveData<String>()

    //PlaceAdapter -> list
    val placeList = ArrayList<Place>()

    //observer val
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    //placeName change
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavePlace() = PlaceDao.getPlace()

    fun isPlaceSave() = PlaceDao.isPlaceSave()
}