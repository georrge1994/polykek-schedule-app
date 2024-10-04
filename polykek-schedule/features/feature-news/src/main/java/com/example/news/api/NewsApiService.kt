package com.example.news.api

import com.android.core.retrofit.api.IApiService
import com.example.news.api.models.RssFeed
import retrofit2.Response
import retrofit2.http.GET

private const val NEWS_RSS_API = "media/news/rss/"

/**
 * News api service.
 */
internal interface NewsApiService : IApiService {
    /**
     * Get media from rss.
     *
     * @return [Response]
     */
    @GET(NEWS_RSS_API)
    suspend fun getMediaFromRss(): Response<RssFeed>
}