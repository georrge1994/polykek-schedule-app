package com.example.news.mvi

import com.android.core.ui.mvi.MviState
import com.example.news.models.NewsItem

/**
 * State for the news screen.
 *
 * @param isLoading True if the news is loading
 * @param news The list of news items
 */
internal data class NewsState(
    val isLoading: Boolean = false,
    val news: List<NewsItem> = emptyList(),
) : MviState {
    internal companion object {
        /**
         * Default state.
         */
        val default = NewsState()
    }
}