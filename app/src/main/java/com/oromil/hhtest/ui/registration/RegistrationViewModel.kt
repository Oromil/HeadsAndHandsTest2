package com.oromil.hhtest.ui.registration

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.UiThread
import com.oromil.hhtest.data.DataManager
import com.oromil.hhtest.data.entities.UserAccount
import com.oromil.hhtest.ui.registration.RegistrationViewModel.InputError.*
import com.oromil.hhtest.utils.*
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(private val dataManager: DataManager) : ViewModel() {

    private val ACCOUNT_NOT_CREATED = -1L

    val accountCreated = MutableLiveData<UserAccount>()
    val incorrectInput = MutableLiveData<InputError>()

    @UiThread
    fun saveUser(email: String, name: String, pass: String, repeatedPass: String) {

        var isDataValid = true
        if (!isEmailValid(email)) {
            incorrectInput.value = INCORRECT_EMAIL
            isDataValid = false
        }
        if (!isNameValid(name)) {
            incorrectInput.value = INCORRECT_NAME
            isDataValid = false
        }
        if (!isPasswordValid(pass)) {
            incorrectInput.value = INCORRECT_PASSWORD
            isDataValid = false
        }
        if (!isDataEqual(pass, repeatedPass)) {
            incorrectInput.value = INCORRECT_REPEAT
            isDataValid = false
        }

        if (!isDataValid)
            return

        val userAccount = encryptUserAccount(UserAccount(email, name, pass))

        ioThenMain({ dataManager.saveUser(userAccount) }) { id ->
            when (id) {
                ACCOUNT_NOT_CREATED -> incorrectInput.value = EMAIL_EXISTS
                else -> accountCreated.value = userAccount
            }
        }
    }

    enum class InputError {
        INCORRECT_EMAIL,
        INCORRECT_PASSWORD,
        INCORRECT_NAME,
        INCORRECT_REPEAT,
        EMAIL_EXISTS
    }
}