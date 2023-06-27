package com.android.core.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Base view model for MVI-pattern.
 *
 * @param I Type of intents
 * @param S Type of states
 * @constructor Create empty constructor for mvi view model
 */
abstract class MviViewModel<I : MviIntent, S : MviState, A : MviAction>(
    defaultState: S
) : ViewModel() {
    val state = MutableStateFlow(defaultState)
    open val action = MutableSharedFlow<A>()

    val currentState: S
        get() = state.value

    /**
     * Dispatch intent async.
     *
     * @param intent Intent
     */
    fun dispatchIntentAsync(intent: I) = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            dispatchIntent(intent)
        }
    }

    /**
     * Dispatch an intent.
     *
     * @param intent Intent
     */
    protected abstract suspend fun dispatchIntent(intent: I)

    /**
     * Emit state.
     *
     * @receiver [S]
     */
    protected suspend fun S.emitState() {
        if (state.value != this) {
            state.emit(this)
        }
    }

    /**
     * Emit action.
     *
     * @receiver [MviAction]
     */
    protected suspend fun A.emitAction() = action.emit(this@emitAction)
}