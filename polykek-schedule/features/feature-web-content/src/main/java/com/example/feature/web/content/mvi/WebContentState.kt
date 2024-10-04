package com.example.feature.web.content.mvi

import com.android.core.ui.mvi.MviState

/**
 * State for the web content screen.
 *
 * @param title The title of the web content
 * @param url The URL of the web content
 * @param scrollToElementClass The specific class to scroll to
 *
 * @constructor Create web content state.
 */
internal data class WebContentState(
    val title: String,
    val url: String,
    val scrollToElementClass: String? = null,
) : MviState {
    internal companion object {
        /**
         * Default state.
         */
        internal val default = WebContentState(
            title = "...",
            url = "",
        )
    }
}