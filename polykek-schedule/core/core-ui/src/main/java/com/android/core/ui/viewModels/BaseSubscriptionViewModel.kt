package com.android.core.ui.viewModels

import androidx.lifecycle.viewModelScope
import com.android.core.ui.mvi.MviAction
import com.android.core.ui.mvi.MviIntent
import com.android.core.ui.mvi.MviState
import com.android.core.ui.mvi.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Base subscription view model.
 *
 * @constructor Create empty constructor for base subscription view model
 */
abstract class BaseSubscriptionViewModel<I : MviIntent, S : MviState, A : MviAction>(
    defaultState: S
) : MviViewModel<I, S, A>(defaultState) {
    private val backgroundJobs = HashMap<Int, Job>()

    /**
     * Async subscribe .
     */
    fun asyncSubscribe() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            subscribe()
        }
    }

    /**
     * Subscribe.
     */
    protected open suspend fun subscribe() {}

    /**
     * Launch in background and save job to cancellation map.
     *
     * @param T Type of flow
     */
    protected fun <T> Flow<T>.cancelableLaunchInBackground() = this.flowOn(Dispatchers.IO)
        .launchIn(viewModelScope)
        .also {
            backgroundJobs[this.hashCode()] = it
        }

    /**
     * Unsubscribe.
     */
    open fun unSubscribe() {
        backgroundJobs.forEach { it.value.cancel() }
        backgroundJobs.clear()
    }

    override fun onCleared() {
        unSubscribe()
        super.onCleared()
    }
}