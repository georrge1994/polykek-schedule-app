package com.example.news.mvi

import com.android.core.ui.mvi.MviIntent

/**
 * Intents for the news screen.
 */
internal sealed class NewsIntent : MviIntent {
    /**
     * Load the content.
     */
    data object LoadContent : NewsIntent()

    /**
     * Select a news item.
     *
     * @param position The position of the news item
     * @param url The URL of the news item
     */
    data class OpenNewsDetail(
        val position: Int,
        val url: String?,
    ) : NewsIntent()
}