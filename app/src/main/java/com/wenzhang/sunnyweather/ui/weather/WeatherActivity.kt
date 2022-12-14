package com.wenzhang.sunnyweather.ui.weather

import android.content.Context
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wenzhang.sunnyweather.R
import com.wenzhang.sunnyweather.databinding.ActivityWeatherBinding
import com.wenzhang.sunnyweather.logic.model.Weather
import com.wenzhang.sunnyweather.logic.model.getSky
import com.wenzhang.sunnyweather.util.LogUtils
import com.wenzhang.sunnyweather.util.showToast
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    lateinit var binding: ActivityWeatherBinding

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val decorView = window.decorView
//        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        window.statusBarColor = Color.TRANSPARENT

//        val controller = ViewCompat.getWindowInsetsController(binding.root)
//        controller?.show(WindowInsetsCompat.Type.statusBars())
//        controller?.hide(WindowInsetsCompat.Type.statusBars())
//        controller?.show(WindowInsetsCompat.Type.navigationBars())
//        controller?.hide(WindowInsetsCompat.Type.navigationBars())
//        controller?.isAppearanceLightStatusBars = true
//        window.statusBarColor = Color.TRANSPARENT

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ??????PlaceActivity?????????????????????,?????????ViewMOdel???
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }

        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }

        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        LogUtils.d(
            "WeatherActivity",
            "placeName:${viewModel.placeName},lng:${viewModel.locationLng},lat:${viewModel.locationLat}"
        )

        binding.now.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })

        // ??????????????????WeatherLiveData??????
        viewModel.weatherLiveData.observe(this) {
            val weather = it.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                "??????????????????????????????".showToast()
                it.exceptionOrNull()?.printStackTrace()
            }
            // ??????????????????????????????????????????????????????????????? -> isRefreshing = false
            binding.swipeRefresh.isRefreshing = false
        }

        // ?????????????????????
        binding.swipeRefresh.setColorSchemeResources(com.google.android.material.R.color.design_dark_default_color_primary)
        //?????????????????????
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        // ??????????????????????????????
        refreshWeather()
    }

    /**
     * ???????????? ??????????????????
     */
    fun refreshWeather() {
        viewModel.refreshWeather()
        // ????????????????????????????????????????????????????????????????????? -> isRefreshing = true
        binding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        val realtime = weather.realtime
        val daily = weather.daily

        // ??????now???????????? ??????????????????
        binding.now.apply {
            placeName.text = viewModel.placeName
            currentTemp.text = "${realtime.temperature.toInt()} ???"
            currentSky.text = getSky(realtime.skycon).info
            currentAQI.text = "???????????? ${realtime.airQuality.aqi.chn.toInt()}"
            nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        }

        // ??????forecast???????????? ????????????????????????
        binding.forecast.apply {
            forecastLayout.removeAllViews()
            val days = daily.skycon.size
            for (i in 0 until days) {
                val skycon = daily.skycon[i]
                val temperature = daily.temperature[i]
                val view = LayoutInflater.from(super.getBaseContext()).inflate(
                    R.layout.forecast_item, forecastLayout, false
                )
                val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
                val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
                val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
                val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
                val simpleDateFormat = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
                val sky = getSky(skycon.value)

                dateInfo.text = simpleDateFormat.format(skycon.date)
                skyIcon.setImageResource(sky.icon)
                skyInfo.text = sky.info
                temperatureInfo.text = "${temperature.min.toInt()} ~ ${temperature.max} ???"
                forecastLayout.addView(view)
            }
        }

        // ??????lifeindex???????????? ??????????????????
        binding.lifeIndex.apply {
            val lifeIndex = daily.lifeIndex
            coldRiskText.text = lifeIndex.coldRisk[0].desc
            dressingText.text = lifeIndex.dressing[0].desc
            ultravioletText.text = lifeIndex.ultraviolet[0].desc
            carWashingText.text = lifeIndex.carWashing[0].desc
        }

        // ????????????????????????
        binding.weatherLayout.visibility = View.VISIBLE
    }
}