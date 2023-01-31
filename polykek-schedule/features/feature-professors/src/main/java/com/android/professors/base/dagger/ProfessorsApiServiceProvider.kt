package com.android.professors.base.dagger

import com.android.professors.search.api.ProfessorsApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
internal class ProfessorsApiServiceProvider {
    @Provides
    @Singleton
    internal fun provideProfessorsApiService(retrofit: Retrofit): ProfessorsApiService = retrofit.create(ProfessorsApiService::class.java)
}