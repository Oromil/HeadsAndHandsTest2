package com.oromil.hhtest.dagger.module

import com.oromil.hhtest.ui.auth.SignInActivity
import com.oromil.hhtest.ui.main.MainActivity
import com.oromil.hhtest.ui.registration.RegistrationActivity
import com.oromil.hhtest.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeSignInActivity(): SignInActivity

    @ContributesAndroidInjector
    abstract fun contributeRegistrationActivity(): RegistrationActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity
}
