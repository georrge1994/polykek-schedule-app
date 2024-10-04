package com.example.news.api

import com.android.common.models.api.Resource
import com.android.core.retrofit.api.common.BaseApiRepository
import com.example.news.api.models.RssFeed
import javax.inject.Inject
import javax.inject.Singleton

/**
 * News api repository.
 *
 * @property newsApiService News api service
 */
@Singleton
internal class NewsApiRepository @Inject constructor(
    private val newsApiService: NewsApiService,
) : BaseApiRepository() {
    /**
     * Get media from rss.
     *
     * @return [Resource]
     */
    suspend fun getMediaFromRss(): Resource<RssFeed> = safeApiCall {
        newsApiService.getMediaFromRss()
    }
}