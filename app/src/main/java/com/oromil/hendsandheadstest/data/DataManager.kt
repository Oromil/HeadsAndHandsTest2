package com.oromil.hendsandheadstest.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.location.Location
import android.support.annotation.WorkerThread
import com.oromil.hendsandheadstest.data.entities.ResponseEntity
import com.oromil.hendsandheadstest.data.entities.UserAccount
import com.oromil.hendsandheadstest.data.entities.Weather
import com.oromil.hendsandheadstest.data.local.PreferencesHelper
import com.oromil.hendsandheadstest.data.local.dao.DataBaseDao
import com.oromil.hendsandheadstest.data.network.NewsApi
import com.oromil.hendsandheadstest.data.network.WeatherApi
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

    fun getNews():LiveData<ResponseEntity?>{
        val data = MutableLiveData<ResponseEntity?>()
        newsApi.getNews()
                .enqueue(object : Callback<ResponseEntity> {
                    override fun onFailure(call: Call<ResponseEntity>, t: Throwable) {
                        data.value = null
                    }

                    override fun onResponse(call: Call<ResponseEntity>, weatherResponce: Response<ResponseEntity>) {
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
    fun getWeather(location: Location): LiveData<Weather?> {
        val data = MutableLiveData<Weather>()
        weatherApi.getWeather(location.latitude.toFloat(),
                location.longitude.toFloat(), "RU")
                .enqueue(object : Callback<Weather> {
                    override fun onFailure(call: Call<Weather>, t: Throwable) {
                        data.value = null
                    }

                    override fun onResponse(call: Call<Weather>, weatherResponce: Response<Weather>) {
                        data.value = weatherResponce.body()
                    }

                })
        return data
    }
}