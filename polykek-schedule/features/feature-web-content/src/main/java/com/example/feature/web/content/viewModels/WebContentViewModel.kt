package com.example.feature.web.content.viewModels

import com.android.core.ui.mvi.MviViewModel
import com.example.feature.web.content.mvi.WebContentAction
import com.example.feature.web.content.mvi.WebContentIntent
import com.example.feature.web.content.mvi.WebContentState
import javax.inject.Inject

/**
 * ViewModel for the web content screen.
 *
 * @constructor Create [WebContentViewModel]
 */
internal class WebContentViewModel @Inject constructor() :
    MviViewModel<WebContentIntent, WebContentState, WebContentAction>(WebContentState.default) {

    override suspend fun dispatchIntent(intent: WebContentIntent) {
        when (intent) {
            is WebContentIntent.LoadWebContent -> updateState(
                intent.title,
                intent.url,
                intent.specificClass
            )
        }
    }

    /**
     * Save data to the state.
     *
     * @param title Title of the web content
     * @param url URL of the web content
     * @param specificClass Specific class to scroll to
     */
    private suspend fun updateState(title: String, url: String, specificClass: String?) {
        currentState.copy(title = title, url = url, scrollToElementClass = specificClass).emitState()
    }
}