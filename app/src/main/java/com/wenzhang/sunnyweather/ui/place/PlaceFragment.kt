package com.wenzhang.sunnyweather.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wenzhang.sunnyweather.MainActivity
import com.wenzhang.sunnyweather.databinding.FragmentPlaceBinding
import com.wenzhang.sunnyweather.ui.weather.WeatherActivity
import com.wenzhang.sunnyweather.util.showToast
import com.wenzhang.sunnyweather.util.startActivity

class PlaceFragment : Fragment() {

    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter: PlaceAdapter

    private lateinit var binding: FragmentPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_place, container, false)
        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)

        checkPlaceSave()

        binding.recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        binding.recyclerView.adapter = adapter

        binding.searchPlaceEdit.addTextChangedListener {
            val content = it.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                setPlaceListVisibilty(false)
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer {
            val places = it.getOrNull()
            if (places != null) {
                setPlaceListVisibilty(true)
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                "未能查询到任何地点".showToast()
                it.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    private fun checkPlaceSave() {
        if (activity is MainActivity && viewModel.isPlaceSave()) {
            val place = viewModel.getSavePlace()
            this.context?.startActivity<WeatherActivity>(
                "location_lng" to place.location.lng,
                "location_lat" to place.location.lat
            )
            this.activity?.finish()
            return
        }
    }

    private fun setPlaceListVisibilty(visibilty: Boolean) {
        if (visibilty) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.bgImageView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.bgImageView.visibility = View.VISIBLE
        }
    }
}