package com.wenzhang.sunnyweather.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wenzhang.sunnyweather.R
import com.wenzhang.sunnyweather.logic.model.Place
import com.wenzhang.sunnyweather.ui.place.PlaceAdapter.*
import com.wenzhang.sunnyweather.ui.weather.WeatherActivity
import com.wenzhang.sunnyweather.util.startActivity

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val placeName: TextView = v.findViewById(R.id.placeName)
        val placeAddress: TextView = v.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val holder = ViewHolder(view)

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            parent.context.startActivity<WeatherActivity>(
                "location_lng" to place.location.lng,
                "location_lat" to place.location.lat
            )
            fragment.viewModel.savePlace(place)
            fragment.activity?.finish()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[holder.adapterPosition]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount(): Int = placeList.size
}