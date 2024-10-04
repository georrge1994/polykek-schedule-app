package com.android.test.support.unitTest.base

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

const val BASE_TEST_URL = "/"

/**
 * Base api repository test.
 *
 * @constructor Create empty Base api repository test
 */
abstract class BaseApiRepositoryTest : BaseUnitTestForSubscriptions() {
    protected val mockWebServer = MockWebServer()

    protected open val defaultClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    /**
     * Get api.
     *
     * @param T Type of return class
     * @param apiServiceClass Api service java class
     * @return Api for service
     */
    protected open fun <T> getApi(apiServiceClass: Class<T>): T = Retrofit.Builder()
        .baseUrl(mockWebServer.url(BASE_TEST_URL))
        .client(defaultClient)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()
        .create(apiServiceClass)

    /**
     * Enqueue response.
     *
     * @param fileName Path + file name with json-response
     * @param code Response code
     * @param header Header of response
     */
    protected fun enqueueResponse(
        fileName: String = "",
        code: Int,
        header: Pair<String, Any>? = null
    ) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("retrofit-api-responses/$fileName")
        val source = inputStream?.let { inputStream.source().buffer() }
        val mockResponse = MockResponse()
            .setResponseCode(code).apply {
                source?.let { setBody(it.readString(StandardCharsets.UTF_8)) }
            }

        header?.let { mockResponse.setHeader(it.first, it.second) }
        mockWebServer.enqueue(mockResponse)
    }

    /**
     * Get last request.
     *
     * @return [RecordedRequest]
     */
    protected fun getRecordedRequest() = mockWebServer.takeRequest()

    /**
     * Close after test.
     */
    override fun afterTest() {
        super.afterTest()
        mockWebServer.shutdown()
    }
}