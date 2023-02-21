package com.android.core.retrofit.impl.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val MAX_AGE = 900 // Seconds.

/**
 * Online caching interceptor.
 *
 * @constructor Create [PolytechOnlineCacheInterceptor]
 */
internal class PolytechOnlineCacheInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request()).newBuilder()
            .header("Cache-Control", "public, max-age=$MAX_AGE")
            .removeHeader("Pragma")
            .build()
}