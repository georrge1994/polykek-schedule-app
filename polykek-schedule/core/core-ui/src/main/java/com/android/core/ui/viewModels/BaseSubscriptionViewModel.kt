package com.android.core.ui.viewModels

import androidx.lifecycle.viewModelScope
import com.android.shared.code.utils.liveData.EventLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn

/**
 * Base subscription view model.
 *
 * @constructor Create empty constructor for base subscription view model
 */
abstract class BaseSubscriptionViewModel : BaseViewModel() {
    private val backgroundJobs = HashMap<Int, Job>()

    val isLoading = EventLiveData<Boolean>()

    /**
     * Async subscribe .
     */
    fun asyncSubscribe() = launchInBackground {
        subscribe()
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
     * Execute with loading animation.
     *
     * @param requestAction Action
     */
    protected fun executeWithLoadingAnimation(requestAction: suspend () -> Unit) = launchInBackground {
        isLoading.postValue(true)
        requestAction.invoke()
        isLoading.postValue(false)
        backgroundJobs.remove(requestAction.hashCode())
    }.also {
        backgroundJobs[requestAction.hashCode()] = it
    }

    /**
     * Unsubscribe.
     */
    open fun unSubscribe() {
        backgroundJobs.forEach { it.value.cancel() }
        backgroundJobs.clear()
    }
}