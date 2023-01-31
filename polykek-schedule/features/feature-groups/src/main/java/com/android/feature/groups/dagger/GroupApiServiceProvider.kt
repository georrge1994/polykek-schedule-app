package com.android.feature.groups.dagger

import com.android.feature.groups.api.GroupApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
internal class GroupApiServiceProvider {
    @Provides
    @Singleton
    internal fun provideGroupApiService(retrofit: Retrofit): GroupApiService = retrofit.create(GroupApiService::class.java)
}