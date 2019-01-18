package com.oromil.hendsandheadstest.ui.main

import android.arch.core.util.Function
import android.arch.lifecycle.*
import android.location.Location
import com.oromil.hendsandheadstest.data.DataManager
import com.oromil.hendsandheadstest.data.GeolocationProvider
import com.oromil.hendsandheadstest.data.entities.StoryEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mDataManager: DataManager,
                                        private val geoProvider: GeolocationProvider,
                                        private val weatherMapper: WeatherMapper)
    : ViewModel() {

    val logout = MutableLiveData<Boolean>()
    val weather = Transformations.switchMap(geoProvider.locationData, Function<Location, LiveData<String?>> { location ->
        return@Function LiveDataReactiveStreams.fromPublisher(mDataManager.getWeather(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { weather ->
                    weatherMapper.mapToString(weather)
                })
    })

    var update: MutableLiveData<Boolean> = MutableLiveData()

    var result: LiveData<List<StoryEntity>> = Transformations
            .switchMap(update) { loadNews() }

    private fun loadNews(): LiveData<List<StoryEntity>> {
        return LiveDataReactiveStreams.fromPublisher(mDataManager.getNews()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())!!)
    }

    fun update() {
        update.value = true
    }

    fun logoutUser() {
        mDataManager.logoutUser()
        logout.value = true
    }

    fun getLocation() {
        geoProvider.getMyLocation()
    }

    fun requestPermissions() = geoProvider.requestPermissions
    fun requestEnable() = geoProvider.requestGeolocationEnable
}