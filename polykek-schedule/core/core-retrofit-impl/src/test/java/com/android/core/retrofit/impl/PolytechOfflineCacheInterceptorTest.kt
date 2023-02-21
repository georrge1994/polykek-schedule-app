
package com.android.core.retrofit.impl

import com.android.core.retrofit.impl.retrofit.PolytechOfflineCacheInterceptor
import com.android.core.retrofit.impl.retrofit.PolytechOnlineCacheInterceptor
import com.android.core.retrofit.impl.retrofit.useCases.NetworkStatusUseCase
import com.android.test.support.unitTest.base.BaseApiRepositoryTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import okhttp3.OkHttpClient
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Polytech cache interceptor test for [PolytechOnlineCacheInterceptor].
 *
 * @constructor Create empty constructor for polytech cache interceptor test
 */
class PolytechOfflineCacheInterceptorTest : BaseApiRepositoryTest() {
    private val networkStatusUseCase: NetworkStatusUseCase = mockk {
        coEvery { isNetworkAvailable() } returns true
    }
    private val polytechOnlineCacheInterceptor = PolytechOfflineCacheInterceptor(networkStatusUseCase)

    override val defaultClient = OkHttpClient.Builder()
        .addInterceptor(polytechOnlineCacheInterceptor)
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    /**
     * Offline. Simple variant - mockk server is freezing with onlyIfCached flag.
     */
    @Test
    fun offline() {
        coEvery { networkStatusUseCase.isNetworkAvailable() } returns false
        enqueueResponse(fileName = "test-200.json", code = 200)
        getApi(TestApi::class.java).test().execute()
        coVerify(exactly = 1) { networkStatusUseCase.isNetworkAvailable() }
    }
}