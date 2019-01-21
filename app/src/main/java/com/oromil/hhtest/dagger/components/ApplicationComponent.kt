package com.oromil.hhtest.dagger.components

import android.app.Application
import com.oromil.hhtest.HeadsAndHandsApp
import com.oromil.hhtest.dagger.module.ActivityModule
import com.oromil.hhtest.dagger.module.ApplicationModule
import com.oromil.hhtest.data.DataManager
import com.oromil.hhtest.ui.base.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ApplicationModule::class, ActivityModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: HeadsAndHandsApp)

    fun dataManager(): DataManager

    fun factory(): ViewModelFactory
}