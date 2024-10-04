package com.example.news.useCases

import com.android.common.models.api.Resource
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.unitTest.base.BaseUnitTestForSubscriptions
import com.example.news.api.NewsApiRepository
import com.example.news.api.models.RssChannel
import com.example.news.api.models.RssEnclosure
import com.example.news.api.models.RssFeed
import com.example.news.api.models.RssItem
import com.example.news.models.NewsItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Unit tests for [NewsUseCase].
 */
class NewsUseCaseTest : BaseUnitTestForSubscriptions() {
    private val htmlParserUseCase: HtmlParserUseCase = mockk()
    private val newsApiRepository: NewsApiRepository = mockk()
    private val backgroundMessageBus = MutableSharedFlow<String>()

    private val newsUseCase = NewsUseCase(
        htmlParserUseCase = htmlParserUseCase,
        newsApiRepository = newsApiRepository,
        backgroundMessageBus = backgroundMessageBus
    )

    // Helper date formats used in NewsUseCase
    private val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
    private val outputFormat = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())

    @Test
    fun `getMediaFromRss returns processed news items`() = runBlockingUnit {
        // Arrange
        val rssItems = listOf(
            RssItem(
                title = "News Title 1",
                description = "<p>Description 1 with <b>HTML</b></p>",
                category = "/Category1/",
                link = "https://example.com/news1",
                pubDate = "Wed, 27 Sep 2023 10:00:00 +0000",
                enclosure = RssEnclosure(url = "https://example.com/image1.jpg")
            ),
            RssItem(
                title = "News Title 2",
                description = "<p>Description 1 with <i>HTML</i></p>",
                category = "/Category2/",
                link = "https://example.com/news2",
                pubDate = "Thu, 28 Sep 2023 11:00:00 +0000",
                enclosure = RssEnclosure(url = "https://example.com/image2.jpg")
            )
        )

        val rssChannel = RssChannel(item = rssItems)
        val rssFeed = RssFeed(channel = rssChannel)

        coEvery { newsApiRepository.getMediaFromRss() } returns Resource.Success(rssFeed)
        coEvery { htmlParserUseCase.removeHtmlCodes(any()) } answers { "Description 1 with HTML" }

        // Act
        val result = newsUseCase.getMediaFromRss()

        // Assert
        val expectedNewsItems = listOf(
            NewsItem(
                title = "News Title 1",
                description = "Description 1 with HTML",
                category = "Category1",
                link = "https://example.com/news1",
                pubDate = convertDate("Wed, 27 Sep 2023 10:00:00 +0000"),
                imageUrl = "https://example.com/image1.jpg"
            ),
            NewsItem(
                title = "News Title 2",
                description = "Description 1 with HTML",
                category = "Category2",
                link = "https://example.com/news2",
                pubDate = convertDate("Thu, 28 Sep 2023 11:00:00 +0000"),
                imageUrl = "https://example.com/image2.jpg"
            )
        )

        assertEquals(expectedNewsItems, result)
    }

    @Test
    fun `getMediaFromRss returns empty list when no items`() = runBlockingUnit {
        // Arrange
        val rssChannel = RssChannel(item = emptyList())
        val rssFeed = RssFeed(channel = rssChannel)

        coEvery { newsApiRepository.getMediaFromRss() } returns Resource.Success(rssFeed)
        coEvery { htmlParserUseCase.removeHtmlCodes(any()) } answers { firstArg() }

        // Act
        val result = newsUseCase.getMediaFromRss()

        // Assert
        assertEquals(emptyList<NewsItem>(), result)
    }

    // Helper function to convert date
    private fun convertDate(input: String): String? {
        return inputFormat.parse(input)?.let {
            outputFormat.format(it)
        }
    }
}
