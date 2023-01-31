package com.android.shared.code.utils.syntaxSugar

import androidx.lifecycle.MutableLiveData

/**
 * Post value if it's changed.
 *
 * @param T Type of data
 * @param newValue New value
 */
fun <T> MutableLiveData<T>.postValueIfChanged(newValue: T) {
    if (this.value != newValue)
        this.postValue(newValue)
}