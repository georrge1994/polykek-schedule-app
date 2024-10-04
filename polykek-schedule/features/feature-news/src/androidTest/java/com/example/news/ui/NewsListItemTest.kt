package com.example.news.ui

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.android.test.support.androidTest.BaseUiComposeTest
import com.example.news.models.NewsItem
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * UI tests for the NewsListItem.
 */
class NewsListItemTest : BaseUiComposeTest() {
    @Test
    fun newsListItem_checkElementsOnTheScreen() {
        var clickedUrl: String? = null

        rule.setContent {
            NewsListItem(
                position = 1,
                newsItem = NewsItem(
                    title = "Title",
                    category = "Category",
                    imageUrl = null,
                    pubDate = "25 September, 16:06",
                    description = "Description",
                    link = "https://www.google.com"
                ),
                onClick = { url ->
                    clickedUrl = url
                }
            )
        }

        rule.onNodeWithText("# 1 Title").assertExists()
        rule.onNodeWithText("Category").assertExists()
        rule.onNodeWithText("25 September, 16:06").assertExists()
        rule.onNodeWithText("Description").assertExists()
        rule.onRoot().performClick()
        assertEquals("https://www.google.com", clickedUrl)
    }
}