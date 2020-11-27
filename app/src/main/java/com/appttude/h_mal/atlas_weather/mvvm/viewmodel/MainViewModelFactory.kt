package com.appttude.h_mal.atlas_weather.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appttude.h_mal.atlas_weather.mvvm.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.mvvm.data.repository.RepositoryImpl

class MainViewModelFactory(
    private val locationProvider: LocationProvider,
    private val repository: RepositoryImpl
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return (MainViewModel(locationProvider, repository)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}