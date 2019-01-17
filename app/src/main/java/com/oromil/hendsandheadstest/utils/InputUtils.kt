package com.oromil.hendsandheadstest.utils

import android.util.Patterns

val PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}"

private fun isDataValid(data: String, pattern: String): Boolean {
    return data.matches(pattern.toRegex())
}

fun isEmailValid(email: String): Boolean {
    return isDataValid(email, Patterns.EMAIL_ADDRESS.pattern())
}

fun isNameValid(name: String): Boolean {
    return name != ""
}

fun isPasswordValid(password: String): Boolean {
    return isDataValid(password, PASSWORD_PATTERN)
}

fun isDataEqual(data1: String?, data2: String?) = data1 == data2
