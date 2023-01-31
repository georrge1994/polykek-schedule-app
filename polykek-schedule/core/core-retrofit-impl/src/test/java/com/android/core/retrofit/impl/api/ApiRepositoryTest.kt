package com.android.core.retrofit.impl.api

import com.android.common.models.api.Resource
import com.android.core.retrofit.api.common.BaseApiRepository
import com.android.test.support.unitTest.base.BaseApiRepositoryTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Test class for base api repository.
 *
 * @constructor Create empty constructor for api repository test
 */
class ApiRepositoryTest : BaseApiRepositoryTest() {
    private val apiRepository = object : BaseApiRepository() {}

    /**
     * HttpException.
     */
    @Test
    fun safeApiCallTest_httpException() = runTest {
        val response = mockk<Response<Any>> {
            coEvery { isSuccessful } returns false
        }
        val result = apiRepository.safeApiCall<Any> { throw HttpException(response) }
        assert(result is Resource.Error)
        assertEquals("Something went wrong", result.message)
    }

    /**
     * IOException.
     */
    @Test
    fun safeApiCallTest_iOException() = runTest {
        val result = apiRepository.safeApiCall<Any> { throw IOException("oops") }
        assert(result is Resource.Error)
        assertEquals("oops", result.message)
    }

    /**
     * NullPointerException.
     */
    @Test
    fun safeApiCallTest_exception() = runTest {
        val result = apiRepository.safeApiCall<Any> { throw java.lang.NullPointerException(null) }
        assert(result is Resource.Error)
        assertEquals("Something went wrong", result.message)
    }

    /**
     * Success.
     */
    @Test
    fun safeApiCallTest_success() = runTest {
        val response = mockk<Response<Any>> {
            coEvery { isSuccessful } returns true
            coEvery { body() } returns mockk()
        }
        val result = apiRepository.safeApiCall { response }
        assert(result is Resource.Success)
        assert(result.data != null)
        assertEquals(null, result.message)
    }
}