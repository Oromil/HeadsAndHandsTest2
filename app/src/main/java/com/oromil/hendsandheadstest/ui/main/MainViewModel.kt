package com.oromil.hendsandheadstest.ui.main

import android.arch.core.util.Function
import android.arch.lifecycle.*
import android.location.Location
import android.util.Log
import com.oromil.hendsandheadstest.data.DataManager
import com.oromil.hendsandheadstest.data.GeolocationProvider
import com.oromil.hendsandheadstest.data.entities.StoryEntity
import com.oromil.hendsandheadstest.data.entities.Weather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mDataManager: DataManager,
                                        private val geoProvider: GeolocationProvider,
                                        private val weatherMapper: WeatherMapper) : ViewModel() {

    val logout = MutableLiveData<Boolean>()

    val weather = Transformations.switchMap(geoProvider.locationData, Function<Location, LiveData<String?>> { location ->
        return@Function Transformations.map(mDataManager.getWeather(location), Function { weatherResponse ->
            if (weatherResponse == null) {
                loadingError.value = Unit
                return@Function null
            }
            weatherMapper.mapToString(weatherResponse)
        })
    })

    private val update: MutableLiveData<Boolean> = MutableLiveData()

    val result: LiveData<List<StoryEntity>> = Transformations
            .switchMap(update) { loadNews() }

    val loadingError = MutableLiveData<Unit>()

    private fun loadNews(): LiveData<List<StoryEntity>> {
        return LiveDataReactiveStreams.fromPublisher(mDataManager.getNews()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnError {
                    loadingError.value = Unit
                }!!)
    }

    fun update() {
        update.value = true
    }

    fun logoutUser() {
        mDataManager.logoutUser()
        logout.value = true
    }

    fun getLocation() {
        geoProvider.updateGeolocation()
    }

    fun requestPermissions() = geoProvider.requestPermissions
    fun requestEnable() = geoProvider.requestGeolocationEnable
}