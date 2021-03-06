package com.appttude.h_mal.atlas_weather.monoWeather.ui.world

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.model.forecast.WeatherDisplay
import com.appttude.h_mal.atlas_weather.monoWeather.ui.EmptyViewHolder
import com.appttude.h_mal.atlas_weather.utils.generateView
import com.appttude.h_mal.atlas_weather.utils.loadImage

class WorldRecyclerAdapter(
        val itemClick: (WeatherDisplay) -> Unit,
        val itemLongClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var weather: MutableList<WeatherDisplay> = mutableListOf()

    fun addCurrent(current: List<WeatherDisplay>){
        weather.clear()
        weather.addAll(current)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (getDataType(viewType)){
            is ViewType.Empty -> {
                val emptyViewHolder = parent.generateView(R.layout.empty_state_layout)
                EmptyViewHolder(emptyViewHolder)
            }
            is ViewType.Current -> {
                val viewCurrent = parent.generateView(R.layout.db_list_item)
                WorldHolderCurrent(viewCurrent)
            }
        }
    }

    sealed class ViewType{
        object Empty : ViewType()
        object Current : ViewType()
    }

    private fun getDataType(type: Int): ViewType {
        return when (type){
            0 -> ViewType.Empty
            1 -> ViewType.Current
            else -> ViewType.Empty
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (weather.isEmpty()) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getDataType(getItemViewType(position))){
            is ViewType.Empty -> {
                holder as EmptyViewHolder
                holder.bindData(null, "World List Empty", "Please add a location")
            }
            is ViewType.Current -> {
                val viewHolderCurrent = holder as WorldHolderCurrent
                val currentWeather = weather[position]
                viewHolderCurrent.bindData(currentWeather)
                viewHolderCurrent.itemView.setOnClickListener {
                    itemClick.invoke(weather[position])
                }
                viewHolderCurrent.itemView.setOnLongClickListener {
                    currentWeather.location?.let { location -> itemLongClick.invoke(location) }
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (weather.size == 0) 1 else weather.size
    }

    internal class WorldHolderCurrent(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        var locationTV: TextView = listItemView.findViewById(R.id.db_location)
        var conditionTV: TextView = listItemView.findViewById(R.id.db_condition)
        var weatherIV: ImageView = listItemView.findViewById(R.id.db_icon)
        var avgTempTV: TextView = listItemView.findViewById(R.id.db_main_temp)
        var tempUnit: TextView = listItemView.findViewById(R.id.db_temp_unit)

        fun bindData(weather: WeatherDisplay?){
            locationTV.text = weather?.displayName
            conditionTV.text = weather?.description
            weatherIV.loadImage(weather?.iconURL)
            avgTempTV.text = weather?.forecast?.get(0)?.mainTemp
            tempUnit.text = itemView.context.getString(R.string.degrees)
        }

    }


}