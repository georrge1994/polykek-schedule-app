package com.android.core.retrofit.api

import com.android.module.injector.moduleMarkers.IModuleApi
import retrofit2.Retrofit

/**
 * Core retrofit module api.
 */
interface ICoreRetrofitModuleApi : IModuleApi {
    /**
     * Provides single retrofit object.
     */
    val retrofit: Retrofit
}