package com.android.feature.schools.dagger

import com.android.feature.schools.api.SchoolApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
internal class SchoolApiServiceProvider {
    @Provides
    @Singleton
    internal fun provideSchoolApiService(retrofit: Retrofit): SchoolApiService = retrofit.create(SchoolApiService::class.java)
}