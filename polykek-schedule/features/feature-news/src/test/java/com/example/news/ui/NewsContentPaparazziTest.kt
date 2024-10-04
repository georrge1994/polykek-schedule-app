package com.example.news.ui

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.core.ui.theme.AppTheme
import com.android.test.support.unitTest.base.BaseUnitTestForSubscriptions
import com.example.news.models.NewsItem
import com.example.news.mvi.NewsState
import org.junit.Rule
import org.junit.Test

/**
 * Paparazzi test for [NewsContent].
 */
class NewsContentPaparazziTest : BaseUnitTestForSubscriptions() {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        maxPercentDifference = 0.001
    )

    @Test
    fun newsContentScreenshotTest_twoItems() {
        paparazzi.snapshot {
            AppTheme {
                NewsContent(
                    state = sampleNewsState(),
                    dispatchIntent = {}
                )
            }
        }
    }

    @Test
    fun newsContentScreenshotTest_emptyList() {
        paparazzi.snapshot {
            AppTheme {
                NewsContent(
                    state = NewsState.default,
                    dispatchIntent = {}
                )
            }
        }
    }

    private fun sampleNewsState(): NewsState = NewsState(
        isLoading = false,
        news = listOf(
            NewsItem(
                title = "Breaking News",
                link = "https://example.com/news1",
                imageUrl = null,
                description = "This is a test description for the news item.",
                category = "Technology",
                pubDate = "25 September, 16:06"
            ),
            NewsItem(
                title = "Breaking News 2",
                link = "https://example.com/news2",
                imageUrl = null,
                description = "This is a test description for the news item 2.",
                category = "Technology 2",
                pubDate = "26 September, 16:06"
            ),
        )
    )
}
