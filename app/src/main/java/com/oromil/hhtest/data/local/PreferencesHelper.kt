package com.oromil.hhtest.data.local

import android.content.Context
import javax.inject.Inject

private const val USER_EMAIL = "user_email"
private const val USER_NAME = "user_name"

class PreferencesHelper @Inject constructor(context: Context) {
    private val preferences = context.getSharedPreferences("com.oromil.hendsandheadstest", Context.MODE_PRIVATE)

    fun saveUserEmail(email: String) {
        preferences.edit().putString(USER_EMAIL, email).apply()
    }

    fun saveUserName(name: String) {
        preferences.edit().putString(USER_NAME, name).apply()
    }

    fun getUserEmail():String = preferences.getString(USER_EMAIL, "")

    fun getUserName():String = preferences.getString(USER_NAME, "")
}