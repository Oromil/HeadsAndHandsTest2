package com.oromil.hhtest.ui.auth

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.UiThread
import com.oromil.hhtest.data.DataManager
import com.oromil.hhtest.data.entities.UserAccount
import com.oromil.hhtest.utils.CryptUtil
import com.oromil.hhtest.utils.ioThenMain
import com.oromil.hhtest.utils.isDataEqual
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val dataManager: DataManager) : ViewModel() {

    val authorizationSuccess = MutableLiveData<Boolean>()

    @UiThread
    fun signIn(email: String, password: String) {

        val tempUserAccount = CryptUtil.encryptUserAccount(UserAccount(email, "", password))

        ioThenMain({ dataManager.getUserAccount(tempUserAccount.email) }) { userAccount ->
            if (userAccount == null) {
                authorizationSuccess.value = false
                return@ioThenMain
            }
            if (isDataEqual(userAccount.password, tempUserAccount.password)) {
                dataManager.loginUserAccount(userAccount)
                authorizationSuccess.value = true
            } else {
                authorizationSuccess.value = false
            }
        }
    }

    fun login(userAccount: UserAccount) {
        dataManager.loginUserAccount(userAccount)
        authorizationSuccess.value = true
    }
}