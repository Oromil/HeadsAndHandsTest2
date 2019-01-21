package com.oromil.hhtest.ui.main

import android.arch.core.util.Function
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.location.Location
import android.support.annotation.UiThread
import com.oromil.hhtest.data.DataManager
import com.oromil.hhtest.data.GeolocationProvider
import com.oromil.hhtest.data.entities.NewsResponseEntity
import com.oromil.hhtest.data.entities.StoryEntity
import com.oromil.hhtest.data.entities.WeatherEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mDataManager: DataManager,
                                        private val geoProvider: GeolocationProvider,
                                        private val weatherMapper: WeatherMapper) : ViewModel() {

    val logout = MutableLiveData<Unit>()

    val weather: LiveData<String?> = Transformations.switchMap(geoProvider.locationData)
    { location ->
        Transformations.map(mDataManager.getWeather(location))
        { weatherResponse: WeatherEntity? ->
            return@map weatherResponse?.let { weatherMapper.mapToString(it) }
                    ?: null.also { loadingError.value = Unit }
        }
    }

    private val update: MutableLiveData<Boolean> = MutableLiveData()

    val news: LiveData<List<StoryEntity>> = Transformations
            .switchMap(update) { loadNews() }

    val loadingError = MutableLiveData<Unit>()

    @UiThread
    private fun loadNews(): LiveData<List<StoryEntity>> {
        return Transformations.map(mDataManager.getNews(),
                Function<NewsResponseEntity?, List<StoryEntity>> { input ->
                    if (input == null) {
                        loadingError.value = Unit
                        return@Function runBlocking {
                            return@runBlocking CoroutineScope(Dispatchers.IO).async {
                                mDataManager.getNewsFromDataBase()
                            }.await()
                        }
                    }
                    return@Function input.results
                })
    }

    fun update() {
        update.value = true
    }

    fun logoutUser() {
        mDataManager.logoutUser()
        logout.value = Unit
    }

    fun getLocation() {
        geoProvider.updateGeolocation()
    }

    fun requestPermissions() = geoProvider.requestPermissions
    fun requestEnable() = geoProvider.requestGeolocationEnable
}