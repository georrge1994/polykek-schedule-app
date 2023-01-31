package com.android.core.ui.dagger

import android.app.Application
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Named

const val BACKGROUND_MESSAGE_BUS = "BACKGROUND_MESSAGE_BUS"

/**
 * Necessary dependencies for core base ui.
 */
interface ICoreUiDependencies {
    /**
     * Application context.
     */
    val application: Application

    /**
     * Background flow for sending messages to the single snackbar observer.
     */
    @Named(BACKGROUND_MESSAGE_BUS)
    val backgroundMessageBus: MutableSharedFlow<String>
}