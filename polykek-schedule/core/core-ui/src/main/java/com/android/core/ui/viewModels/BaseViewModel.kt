package com.android.core.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Base view model.
 *
 * @constructor Create empty constructor for base view model
 */
abstract class BaseViewModel : ViewModel() {
    /**
     * Launch in background.
     *
     * @param requestAction Request action
     */
    protected fun launchInBackground(requestAction: suspend () -> Unit) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            requestAction.invoke()
        }
    }
}