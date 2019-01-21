package com.oromil.hhtest.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.location.Location
import android.support.annotation.WorkerThread
import com.oromil.hhtest.data.entities.NewsResponseEntity
import com.oromil.hhtest.data.entities.UserAccount
import com.oromil.hhtest.data.entities.WeatherEntity
import com.oromil.hhtest.data.local.PreferencesHelper
import com.oromil.hhtest.data.local.dao.DataBaseDao
import com.oromil.hhtest.data.network.NewsApi
import com.oromil.hhtest.data.network.WeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(private val newsApi: NewsApi,
                                      private val weatherApi: WeatherApi,
                                      private val dataBaseDao: DataBaseDao,
                                      private val sharedPreferences: PreferencesHelper) {

    fun getNews():LiveData<NewsResponseEntity?>{
        val data = MutableLiveData<NewsResponseEntity?>()
        newsApi.getNews()
                .enqueue(object : Callback<NewsResponseEntity> {
                    override fun onFailure(call: Call<NewsResponseEntity>, t: Throwable) {
                        data.value = null
                    }

                    override fun onResponse(call: Call<NewsResponseEntity>, weatherResponce: Response<NewsResponseEntity>) {
                        data.value = weatherResponce.body()
                    }

                })
        return data
    }

    fun getNewsFromDataBase() = dataBaseDao.getAll()

    @WorkerThread
    fun saveUser(userAccount: UserAccount) = dataBaseDao.saveUser(userAccount)

    fun loginUserAccount(userAccount: UserAccount) {
        sharedPreferences.saveUserEmail(userAccount.email)
        sharedPreferences.saveUserName(userAccount.name)
    }

    fun logoutUser() {
        sharedPreferences.saveUserName("")
        sharedPreferences.saveUserEmail("")
    }

    @WorkerThread
    fun getUserAccount(email: String) = dataBaseDao.getUserWithEmail(email)

    fun getLoginedUserEmail() = sharedPreferences.getUserEmail()
    fun getLoggedUserName() = sharedPreferences.getUserName()

    @WorkerThread
    fun getWeather(location: Location): LiveData<WeatherEntity?> {
        val data = MutableLiveData<WeatherEntity>()
        weatherApi.getWeather(location.latitude.toFloat(),
                location.longitude.toFloat(), "RU")
                .enqueue(object : Callback<WeatherEntity> {
                    override fun onFailure(call: Call<WeatherEntity>, t: Throwable) {
                        data.value = null
                    }

                    override fun onResponse(call: Call<WeatherEntity>, weatherResponce: Response<WeatherEntity>) {
                        data.value = weatherResponce.body()
                    }

                })
        return data
    }
}