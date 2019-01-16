package com.oromil.hendsandheadstest.utils

import io.reactivex.disposables.Disposable

fun dispose(disposable: Disposable) {
    if (!disposable.isDisposed)
        disposable.dispose()
}