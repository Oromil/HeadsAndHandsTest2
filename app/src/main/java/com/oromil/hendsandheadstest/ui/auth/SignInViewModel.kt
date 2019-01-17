package com.oromil.hendsandheadstest.ui.auth

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.UiThread
import com.oromil.hendsandheadstest.data.DataManager
import com.oromil.hendsandheadstest.utils.ioThenMain
import com.oromil.hendsandheadstest.utils.isDataEqual
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val dataManager: DataManager) : ViewModel() {

    //    val authorizationError = MutableLiveData<Boolean>()
    val authorizationSuccess = MutableLiveData<Boolean>()

    @UiThread
    fun signIn(email: String, password: String) {
        ioThenMain({ dataManager.getUserAccount(email) }) { userAccount ->
            if (userAccount == null) {
                authorizationSuccess.value = false
                return@ioThenMain
            }
            if (isDataEqual(userAccount.password, password)) {
                dataManager.loginUserAccount(userAccount)
                authorizationSuccess.value = true
            } else {
                authorizationSuccess.value = false
            }
        }
    }
}