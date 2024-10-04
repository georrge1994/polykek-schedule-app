package com.example.news.dagger

import com.example.news.api.NewsApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
internal class NewsApiServiceProvider {
    @Provides
    @Singleton
    internal fun provideBuildingApiService(retrofit: Retrofit): NewsApiService =
        retrofit.create(NewsApiService::class.java)
}