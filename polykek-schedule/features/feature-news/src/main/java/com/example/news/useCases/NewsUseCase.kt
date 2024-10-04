package com.example.news.useCases

import com.android.core.retrofit.api.common.CatchResourceUseCase
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.example.news.api.NewsApiRepository
import com.example.news.models.NewsItem
import kotlinx.coroutines.flow.MutableSharedFlow
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

/**
 * News use case.
 *
 * @param htmlParserUseCase The use case for parsing html
 * @param newsApiRepository The repository for the news screen
 * @param backgroundMessageBus The background message bus
 * @constructor Create instance of [NewsUseCase]
 */
internal class NewsUseCase @Inject constructor(
    private val htmlParserUseCase: HtmlParserUseCase,
    private val newsApiRepository: NewsApiRepository,
    @Named(BACKGROUND_MESSAGE_BUS) backgroundMessageBus: MutableSharedFlow<String>
) : CatchResourceUseCase(backgroundMessageBus) {
    private val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
    private val outputFormat = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())

    internal suspend fun getMediaFromRss(): List<NewsItem>? = newsApiRepository.getMediaFromRss().catchRequestError {
        it.channel?.item?.map { rssItem ->
            NewsItem(
                title = rssItem.title,
                description = htmlParserUseCase.removeHtmlCodes(rssItem.description),
                category = rssItem.category?.replace("/",""),
                link = rssItem.link,
                pubDate = rssItem.pubDate?.convertDate(),
                imageUrl = rssItem.enclosure?.url,
            )
        } ?: emptyList()
    }


    /**
     * Convert date to another format.
     *
     * @receiver The date to convert
     * @return The converted date
     */
    private fun String.convertDate(): String? = inputFormat.parse(this)?.let {
        outputFormat.format(it)
    }
}