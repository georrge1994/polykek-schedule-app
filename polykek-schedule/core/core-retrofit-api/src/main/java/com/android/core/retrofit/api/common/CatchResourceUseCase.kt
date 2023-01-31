package com.android.core.retrofit.api.common

import com.android.common.models.api.Resource
import com.android.shared.code.utils.markers.IUseCase
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Provides general logic for catching [Resource].
 *
 * @property backgroundMessageBus Common background message bus with snack-bar output
 * @constructor Create [CatchResourceUseCase]
 */
abstract class CatchResourceUseCase(
    private val backgroundMessageBus: MutableSharedFlow<String>
) : IUseCase {
    /**
     * Catch request error and send it to common bus.
     *
     * @param T Type of message
     * @param action Request action
     * @return [Resource]
     */
    protected suspend fun <T, R> Resource<T>.catchRequestError(action: suspend (data: T) -> R): R? = when (this) {
        is Resource.Success -> action.invoke(data!!)
        is Resource.Error -> {
            backgroundMessageBus.emit(message!!)
            null
        }
    }
}