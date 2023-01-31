package com.android.feature.buildings.dagger

import com.android.feature.buildings.api.BuildingApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
internal class BuildingApiServiceProvider {
    @Provides
    @Singleton
    internal fun provideBuildingApiService(retrofit: Retrofit): BuildingApiService = retrofit.create(BuildingApiService::class.java)
}