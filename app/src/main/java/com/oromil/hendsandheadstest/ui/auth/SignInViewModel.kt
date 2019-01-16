package com.oromil.hendsandheadstest.ui.auth

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.UiThread
import com.oromil.hendsandheadstest.data.DataManager
import com.oromil.hendsandheadstest.data.local.PreferencesHelper
import com.oromil.hendsandheadstest.utils.ioThenMain
import com.oromil.hendsandheadstest.utils.isDataEqual
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val dataManager: DataManager,
                                          private val preferencesHelper: PreferencesHelper) : ViewModel() {

    val authorizationError = MutableLiveData<Boolean>()
    val authorizationSuccess = MutableLiveData<Boolean>()

    @UiThread
    fun signIn(email: String, password: String) {
        ioThenMain({ dataManager.getUserAccount(email) }) { userAccount ->
            userAccount ?: return@ioThenMain
            if (isDataEqual(userAccount.password, password)) {
                authorizationSuccess.value = true
            } else {

            }
        }
    }
}