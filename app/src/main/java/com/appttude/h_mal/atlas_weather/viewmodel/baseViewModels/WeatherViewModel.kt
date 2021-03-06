package com.appttude.h_mal.atlas_weather.viewmodel.baseViewModels

import androidx.lifecycle.ViewModel
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.model.weather.FullWeather

abstract class WeatherViewModel : ViewModel(){

    fun createFullWeather(
            weather: WeatherResponse,
            location: String
    ): FullWeather {
        return FullWeather(weather).apply {
            temperatureUnit = "°C"
            locationString = location
        }
    }

    fun createWeatherEntity(
            locationId: String,
            weather: FullWeather
    ): EntityItem{
        weather.apply {
            locationString = locationId
        }

        return EntityItem(locationId, weather)
    }
}