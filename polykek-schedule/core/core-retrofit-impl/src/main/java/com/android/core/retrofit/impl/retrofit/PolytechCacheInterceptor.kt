package com.android.core.retrofit.impl.retrofit

import com.android.core.retrofit.impl.retrofit.useCases.NetworkStatusUseCase
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Cache interceptor.
 *
 * @property networkStatusUseCase Provides actual status of network
 * @constructor Create [PolytechCacheInterceptor]
 */
internal class PolytechCacheInterceptor @Inject constructor(private val networkStatusUseCase: NetworkStatusUseCase) : Interceptor {
    private val onlineCacheControl by lazy {
        CacheControl.Builder()
            .maxAge(15, TimeUnit.MINUTES)
            .build()
    }

    private val offlineCacheControl by lazy {
        CacheControl.Builder()
            .maxStale(1, TimeUnit.DAYS)
            .onlyIfCached()
            .build()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = if (networkStatusUseCase.isNetworkAvailable()) {
            request.newBuilder().cacheControl(onlineCacheControl).build()
        } else {
            request.newBuilder().cacheControl(offlineCacheControl).build()
        }
        return chain.proceed(request)
    }
}