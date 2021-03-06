package com.appttude.h_mal.atlas_weather.application

import android.app.Application
import androidx.test.espresso.idling.CountingIdlingResource
import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.network.WeatherApi
import com.appttude.h_mal.atlas_weather.data.network.interceptors.NetworkConnectionInterceptor
import com.appttude.h_mal.atlas_weather.data.network.interceptors.QueryParamsInterceptor
import com.appttude.h_mal.atlas_weather.data.network.networkUtils.loggingInterceptor
import com.appttude.h_mal.atlas_weather.data.prefs.PreferenceProvider
import com.appttude.h_mal.atlas_weather.data.repository.RepositoryImpl
import com.appttude.h_mal.atlas_weather.data.repository.SettingsRepositoryImpl
import com.appttude.h_mal.atlas_weather.data.room.AppDatabase
import com.appttude.h_mal.atlas_weather.helper.ServicesHelper
import com.appttude.h_mal.atlas_weather.viewmodel.ApplicationViewModelFactory
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

const val LOCATION_PERMISSION_REQUEST = 505

class AppClass : Application(), KodeinAware {

    companion object {
        // idling resource to be used for espresso testing
        // when we need to wait for async operations to complete
        val idlingResources = CountingIdlingResource("Data_loader")
    }

    // Kodein creation of modules to be retrieve within the app
    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppClass))

        bind() from singleton { Gson() }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { QueryParamsInterceptor() }
        bind() from singleton { loggingInterceptor }
        bind() from singleton { WeatherApi("https://api.openweathermap.org/data/2.5/", instance(), instance(), instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { RepositoryImpl(instance(), instance(), instance()) }
        bind() from singleton { SettingsRepositoryImpl(instance()) }
        bind() from singleton { LocationProviderImpl(instance()) }
        bind() from singleton { ServicesHelper(instance(), instance(), instance()) }
        bind() from provider { ApplicationViewModelFactory(instance(), instance()) }
    }

}