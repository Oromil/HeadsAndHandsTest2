package com.oromil.hendsandheadstest.dagger.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.oromil.hendsandheadstest.dagger.ViewModelKey
import com.oromil.hendsandheadstest.ui.auth.SignInViewModel
import com.oromil.hendsandheadstest.ui.base.ViewModelFactory
import com.oromil.hendsandheadstest.ui.main.MainViewModel
import com.oromil.hendsandheadstest.ui.registration.RegistrationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun signInViewModel(viewModel: SignInViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    internal abstract fun registrationViewModel(viewModel: RegistrationViewModel):ViewModel
}