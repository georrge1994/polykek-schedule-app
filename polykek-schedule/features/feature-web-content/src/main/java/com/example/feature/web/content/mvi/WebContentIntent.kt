package com.example.feature.web.content.mvi

import com.android.core.ui.mvi.MviIntent

/**
 * Intents for the web content screen.
 */
internal sealed class WebContentIntent : MviIntent {
    /**
     * Load web content.
     *
     * @param title The title of the web content
     * @param url The URL of the web content
     * @constructor Create load web content intent.
     */
    data class LoadWebContent(
        val title: String,
        val url: String,
        val specificClass: String?,
    ) : WebContentIntent()
}