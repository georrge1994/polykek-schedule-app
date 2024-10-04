package com.example.news.viewModels

import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.example.news.models.NewsItem
import com.example.news.mvi.NewsIntent
import com.example.news.mvi.NewsState
import com.example.news.useCases.NewsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests for [NewsViewModel].
 *
 * @constructor Create empty News view model test
 */
class NewsViewModelTest : BaseViewModelUnitTest() {
    private val newsUseCase: NewsUseCase = mockk()
    private val newsViewModel = NewsViewModel(newsUseCase)

    /**
     * Check [NewsIntent.LoadContent].
     */
    @Test
    fun loadContent() = runBlockingUnit {
        val newsItems = listOf(
            NewsItem(
                title = "Sample News Title",
                description = "Sample Description",
                category = "Sample Category",
                link = "https://example.com/news",
                pubDate = "27 September, 10:00",
                imageUrl = "https://example.com/image.jpg"
            )
        )
        coEvery { newsUseCase.getMediaFromRss() } returns newsItems
        newsViewModel.state.collectPost {
            newsViewModel.dispatchIntentAsync(NewsIntent.LoadContent).joinWithTimeout()
        }.apply {
            assertEquals(3, this.size)
            this[0].checkState(isLoading = false, countOfItems = 0)
            this[1].checkState(isLoading = true, countOfItems = 0)
            this[2].checkState(isLoading = false, countOfItems = 1)
        }
    }

    /**
     * Check [NewsIntent.OpenNewsDetail].
     */
    @Test
    fun openDetails() = runBlockingUnit {
        newsViewModel.state.collectPost {
            newsViewModel.dispatchIntentAsync(NewsIntent.OpenNewsDetail(0, "https://example.com/news")).joinWithTimeout()
        }.apply {
            assertEquals(1, this.size)
            this[0].checkState(isLoading = false, countOfItems = 0)
        }
    }

    /**
     * Check state.
     *
     * @receiver [NewsState] or null
     * @param isLoading Is loading
     * @param countOfItems Count of items
     */
    private fun NewsState?.checkState(isLoading: Boolean, countOfItems: Int) {
        this ?: throw AssertionError("State is null")
        assertEquals(isLoading, this.isLoading)
        assertEquals(countOfItems, this.news.size)
    }
}
