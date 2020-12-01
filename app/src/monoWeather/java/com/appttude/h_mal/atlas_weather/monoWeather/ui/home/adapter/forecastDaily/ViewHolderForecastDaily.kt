package com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.forecastDaily

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.Forecast
import com.appttude.h_mal.atlas_weather.utils.loadImage

class ViewHolderForecastDaily(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var dateTV: TextView = itemView.findViewById(R.id.list_date)
    var dayTV: TextView = itemView.findViewById(R.id.list_day)
    var weatherIV: ImageView = itemView.findViewById(R.id.list_icon)
    var mainTempTV: TextView = itemView.findViewById(R.id.list_main_temp)

    fun bindView(forecast: Forecast?) {
        dateTV.text = forecast?.date
        dayTV.text = forecast?.day
        weatherIV.loadImage(forecast?.weatherIcon, 64, 64)
        mainTempTV.text = forecast?.mainTemp
    }
}