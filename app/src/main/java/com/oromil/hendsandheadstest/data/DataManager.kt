package com.oromil.hendsandheadstest.data

import android.support.annotation.WorkerThread
import com.oromil.hendsandheadstest.data.entities.MultimediaEntity
import com.oromil.hendsandheadstest.data.entities.StoryEntity
import com.oromil.hendsandheadstest.data.entities.UserAccount
import com.oromil.hendsandheadstest.data.local.PreferencesHelper
import com.oromil.hendsandheadstest.data.local.dao.DataBaseDao
import com.oromil.hendsandheadstest.data.network.Api
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(private val api: Api, private val dataBaseDao: DataBaseDao,
                                      private val sharedPreferences: PreferencesHelper) {

    fun getNews(): Flowable<List<StoryEntity>>? {
        return api.getNews().map { response ->
            response.results.forEach { storyEntity: StoryEntity ->
                if (storyEntity.multimedia.isEmpty())
                    storyEntity.multimedia.add(MultimediaEntity())
            }
            response.results
        }.map { t: List<StoryEntity> ->
            dataBaseDao.insert(t)
            t
        }.onErrorReturn { dataBaseDao.getAll() }
                .toFlowable(BackpressureStrategy.BUFFER)
    }

    @WorkerThread
    fun saveUser(userAccount: UserAccount) = dataBaseDao.saveUser(userAccount)

    fun loginUserAccount(userAccount: UserAccount) {
        sharedPreferences.saveUserEmail(userAccount.email)
        sharedPreferences.saveUserName(userAccount.name)
    }

    fun logoutUser(){
        sharedPreferences.saveUserName("")
        sharedPreferences.saveUserEmail("")
    }

    @WorkerThread
    fun getUserAccount(email:String) = dataBaseDao.getUserWithEmail(email)

    fun getLoginedUserEmail() = sharedPreferences.getUserEmail()
    fun getLoggedUserName() = sharedPreferences.getUserName()
}