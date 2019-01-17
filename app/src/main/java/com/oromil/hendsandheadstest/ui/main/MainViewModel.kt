package com.oromil.hendsandheadstest.ui.main

import android.arch.lifecycle.*
import com.oromil.hendsandheadstest.data.DataManager
import com.oromil.hendsandheadstest.data.entities.StoryEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mDataManager: DataManager)
    : ViewModel() {

    val logout = MutableLiveData<Boolean>()
    val weather = MutableLiveData<String>()

    var update: MutableLiveData<Boolean> = MutableLiveData()

    var result: LiveData<List<StoryEntity>> = Transformations
            .switchMap(update) { loadNews() }

    private fun loadNews(): LiveData<List<StoryEntity>> {
        return LiveDataReactiveStreams.fromPublisher(mDataManager.getNews()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())!!)
    }

    fun loadWether() {
        val req = mDataManager.getWeather()
        try {

            CoroutineScope(Dispatchers.Main).async{
                val result = req.awaitResult()
                when(result){
                    is Result.Ok->{
                        val weatherResponse = result.value
                        weather.value = "Now in "+weatherResponse.name+" "+weatherResponse.main.temp + "C, "
                    }
                    is Result.Error->{}
                    is Result.Exception->{}
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
//        ioThenMain({ mDataManager.getWeather() }) { call ->
//            call ?: return@ioThenMain
//            call.enqueue(object : Callback<Weather> {
//                override fun onFailure(call: Call<Weather>, t: Throwable) {
//
//                }
//
//                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
//                    Log.d("Weather", (response.body() as Weather).name)
//                }
//            })
//        }
    }

    fun update() {
        update.value = true
    }

    fun logoutUser() {
        mDataManager.logoutUser()
        logout.value = true
    }
}