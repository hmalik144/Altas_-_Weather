package com.appttude.h_mal.atlas_weather.monoWeather.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.monoWeather.ui.home.adapter.WeatherRecyclerAdapter
import com.appttude.h_mal.atlas_weather.utils.navigateTo
import com.appttude.h_mal.atlas_weather.viewmodel.WorldViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class WorldItemFragment : BaseFragment() {

    private val viewModel by getFragmentViewModel<WorldViewModel>()
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param1 = WorldItemFragmentArgs.fromBundle(requireArguments()).locationName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = WeatherRecyclerAdapter {
            val directions =
                    WorldItemFragmentDirections.actionWorldItemFragmentToFurtherDetailsFragment(it)
            navigateTo(directions)
        }

        param1?.let { viewModel.getSingleLocation(it) }

        viewModel.singleWeatherLiveData.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.addCurrent(it)
            swipe_refresh.isRefreshing = false
        })

        forecast_listview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        swipe_refresh.apply {
            setOnRefreshListener {
                param1?.let {
                    viewModel.fetchDataForSingleLocation(it)
                    isRefreshing = true
                }
            }
        }

        viewModel.operationState.observe(viewLifecycleOwner, progressBarStateObserver(progressBar))
        viewModel.operationError.observe(viewLifecycleOwner, errorObserver())
        viewModel.operationRefresh.observe(viewLifecycleOwner, refreshObserver(swipe_refresh))
    }

}