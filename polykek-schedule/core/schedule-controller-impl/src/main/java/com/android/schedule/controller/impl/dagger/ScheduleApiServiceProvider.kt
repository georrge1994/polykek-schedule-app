package com.android.schedule.controller.impl.dagger

import com.android.schedule.controller.impl.api.ScheduleApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
internal class ScheduleApiServiceProvider {
    @Provides
    @Singleton
    internal fun provideScheduleApiService(retrofit: Retrofit): ScheduleApiService = retrofit.create(ScheduleApiService::class.java)
}