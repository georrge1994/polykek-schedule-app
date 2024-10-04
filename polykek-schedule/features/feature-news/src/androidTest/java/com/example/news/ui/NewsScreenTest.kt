package com.example.news.ui

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import com.android.feature.news.R
import com.android.test.support.androidTest.BaseUiComposeTest
import com.example.news.models.NewsItem
import com.example.news.mvi.NewsState
import org.junit.Test

/**
 * UI tests for the News screen.
 */
class NewsScreenTest : BaseUiComposeTest() {
    @Test
    fun newsContent_loadingState_displaysRefreshIndicator() {
        // Given
        val state = NewsState(
            isLoading = true,
            news = emptyList()
        )

        rule.setContent {
            NewsContent(
                state = state,
                dispatchIntent = {}
            )
        }

        rule.onNodeWithTag("PullRefreshIndicator").assertExists()
    }

    @Test
    fun newsContent_emptyNews_showsEmptyMessage() {
        // Given
        val state = NewsState(
            isLoading = false,
            news = emptyList()
        )

        // When
        rule.setContent {
            NewsContent(
                state = state,
                dispatchIntent = {}
            )
        }

        // Then
        val emptyMessage = context.getString(R.string.news_fragment_empty_list)
        rule
            .onNodeWithTag("EmptyListMessage")
            .assertExists()
            .assertTextEquals(emptyMessage)
    }

    @Test
    fun newsContent_withNewsItems_displaysNewsList() {
        // Given
        val newsItems = listOf(
            NewsItem(
                title = "Sample News Title 1",
                link = "https://example.com/news1",
                imageUrl = null,
                description = "Description 1",
                category = "Category 1",
                pubDate = "25 September, 16:06"
            ),
            NewsItem(
                title = "Sample News Title 2",
                link = "https://example.com/news2",
                imageUrl = null,
                description = "Description 2",
                category = "Category 2",
                pubDate = "26 September, 10:00"
            )
        )
        val state = NewsState(
            isLoading = false,
            news = newsItems
        )

        // When
        rule.setContent {
            NewsContent(
                state = state,
                dispatchIntent = {}
            )
        }

        // Then
        newsItems.forEachIndexed { index, _ ->
            rule.onNodeWithTag("NewsListItem_${index + 1}").assertExists()
        }
    }
}