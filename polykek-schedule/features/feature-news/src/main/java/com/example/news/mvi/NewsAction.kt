package com.example.news.mvi

import com.android.core.ui.mvi.MviAction

/**
 * Actions for the news screen.
 */
internal sealed class NewsAction : MviAction {
    /**
     * Open the news item.
     *
     * @param position The position of the news item
     * @param url The URL of the news item
     */
    data class OpenItem(
        val position: Int,
        val url: String
    ) : NewsAction()
}