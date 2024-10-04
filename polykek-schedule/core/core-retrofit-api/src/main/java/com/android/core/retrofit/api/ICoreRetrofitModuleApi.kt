package com.android.core.retrofit.api

import com.android.module.injector.moduleMarkers.IModuleApi
import retrofit2.Retrofit
import javax.inject.Named

const val JSON_RETROFIT = "Retrofit"
const val RSS_MEDIA_RETROFIT = "RetrofitRssMedia"

/**
 * Core retrofit module api.
 */
interface ICoreRetrofitModuleApi : IModuleApi {
    /**
     * Provides JSON retrofit object.
     */
    @get:Named(JSON_RETROFIT)
    val retrofit: Retrofit

    /**
     * Provides RSS media retrofit object.
     */
    @get:Named(RSS_MEDIA_RETROFIT)
    val rssMediaRetrofit: Retrofit
}