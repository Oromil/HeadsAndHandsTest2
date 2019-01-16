package com.oromil.hendsandheadstest.dagger.components

import android.app.Application
import com.oromil.hendsandheadstest.BoilerplateApp
import com.oromil.hendsandheadstest.dagger.module.ActivityModule
import com.oromil.hendsandheadstest.dagger.module.ApplicationModule
import com.oromil.hendsandheadstest.data.DataManager
import com.oromil.hendsandheadstest.ui.base.ViewModelFactory
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

    fun inject(application: BoilerplateApp)

    fun dataManager(): DataManager

    fun factory(): ViewModelFactory
}