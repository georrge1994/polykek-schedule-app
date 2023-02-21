package com.android.core.retrofit.impl.retrofit

import com.android.core.retrofit.impl.retrofit.useCases.NetworkStatusUseCase
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Offline cache interceptor.
 *
 * @property networkStatusUseCase Provides actual status of network
 * @constructor Create [PolytechOfflineCacheInterceptor]
 */
internal class PolytechOfflineCacheInterceptor @Inject constructor(
    private val networkStatusUseCase: NetworkStatusUseCase
) : Interceptor {
    private val offlineCacheControl by lazy {
        CacheControl.Builder()
            .maxStale(1, TimeUnit.DAYS)
            .onlyIfCached()
            .build()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!networkStatusUseCase.isNetworkAvailable()) {
            request = request.newBuilder().cacheControl(offlineCacheControl).build()
        }
        return chain.proceed(request)
    }
}