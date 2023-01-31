package com.android.core.retrofit.impl

import com.android.core.retrofit.impl.retrofit.useCases.NetworkStatusUseCase
import com.android.core.retrofit.impl.retrofit.PolytechCacheInterceptor
import com.android.test.support.unitTest.base.BaseApiRepositoryTest
import com.google.common.net.HttpHeaders
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Call
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

/**
 * Polytech cache interceptor test for [PolytechCacheInterceptor].
 *
 * @constructor Create empty constructor for polytech cache interceptor test
 */
class PolytechCacheInterceptorTest : BaseApiRepositoryTest() {
    private val networkStatusUseCase: NetworkStatusUseCase = mockk {
        coEvery { isNetworkAvailable() } returns true
    }
    private val polytechCacheInterceptor = PolytechCacheInterceptor(networkStatusUseCase)

    override val defaultClient = OkHttpClient.Builder()
        .addInterceptor(polytechCacheInterceptor)
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    /**
     * Online.
     */
    @Test
    fun online() {
        coEvery { networkStatusUseCase.isNetworkAvailable() } returns true
        enqueueResponse(fileName = "test-200.json", code = 200)
        getApi(TestApi::class.java).test().execute()
        val recordedRequest = getRecordedRequest()
        val header = recordedRequest.getHeader(HttpHeaders.CACHE_CONTROL)
        assertEquals("max-age=900", header)
        coVerify(exactly = 1) { networkStatusUseCase.isNetworkAvailable() }
    }

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

/**
 * Test api.
 */
private interface TestApi {
    @GET("/test")
    fun test(): Call<TestData>
}

/**
 * Test data.
 *
 * @property name Some property
 * @constructor Create [TestData]
 */
private data class TestData(val name: String)