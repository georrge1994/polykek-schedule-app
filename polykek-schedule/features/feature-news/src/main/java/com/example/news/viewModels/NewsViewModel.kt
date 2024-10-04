package com.example.news.viewModels

import androidx.lifecycle.viewModelScope
import com.android.core.ui.mvi.MviViewModel
import com.example.news.mvi.NewsAction
import com.example.news.mvi.NewsIntent
import com.example.news.mvi.NewsState
import com.example.news.useCases.NewsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the news screen.
 *
 * @param newsUseCase The use case for the news screen
 *
 * @constructor Create [NewsViewModel]
 */
internal class NewsViewModel @Inject constructor(
    private val newsUseCase: NewsUseCase
) : MviViewModel<NewsIntent, NewsState, NewsAction>(NewsState.default) {

    override suspend fun dispatchIntent(intent: NewsIntent) {
        when (intent) {
            is NewsIntent.LoadContent -> loadContent()
            is NewsIntent.OpenNewsDetail -> clickByItem(intent.position, intent.url)
        }
    }

    /**
     * Load the content.
     */
    private suspend fun loadContent() {
        currentState.copy(isLoading = true).emitState()
        newsUseCase.getMediaFromRss()?.let {
            currentState.copy(news = it, isLoading = false).emitState()
        } ?: currentState.copy(isLoading = false).emitState()
    }

    /**
     * Click by news item.
     *
     * @param position The position of the news item
     * @param contentUrl The URL of the news item
     */
    private fun clickByItem(position: Int, contentUrl: String?) = viewModelScope.launch {
        contentUrl ?: return@launch
        NewsAction.OpenItem(position, contentUrl).emitAction()
    }
}