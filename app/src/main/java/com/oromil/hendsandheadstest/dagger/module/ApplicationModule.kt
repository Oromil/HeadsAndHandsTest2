package com.oromil.hendsandheadstest.dagger.module

import android.app.Application
import android.arch.persistence.room.Room
import com.oromil.hendsandheadstest.data.local.AppDataBase
import com.oromil.hendsandheadstest.data.local.PreferencesHelper
import com.oromil.hendsandheadstest.data.network.Api
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provide application-level dependencies.
 */

@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    private val DATABASE_NAME: String = "database"

    @Provides
    @Singleton
    internal fun provideApiService(): Api {
        return Api.create()
    }

    @Singleton
    @Provides
    fun provideDataBase(application: Application): AppDataBase {
        return Room.databaseBuilder(application, AppDataBase::class.java, DATABASE_NAME).build()
    }

    @Singleton
    @Provides
    fun providesDao(database: AppDataBase) = database.mDao()

    @Singleton
    @Provides
    fun providePreferences(application: Application) = PreferencesHelper(application)
}