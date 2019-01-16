package com.oromil.hendsandheadstest.dagger.module

import com.oromil.hendsandheadstest.ui.auth.SignInActivity
import com.oromil.hendsandheadstest.ui.main.MainActivity
import com.oromil.hendsandheadstest.ui.registration.RegistrationActivity
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
}
