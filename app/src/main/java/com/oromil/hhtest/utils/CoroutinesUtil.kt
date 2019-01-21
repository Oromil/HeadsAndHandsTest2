package com.oromil.hhtest.utils

import kotlinx.coroutines.*

fun <T: Any> ioThenMain(work: suspend (() -> T?), callback: ((T?) -> Unit)? = null): Job =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async {
                return@async work()
            }.await()
            callback?.let {
                it(data)
            }
        }