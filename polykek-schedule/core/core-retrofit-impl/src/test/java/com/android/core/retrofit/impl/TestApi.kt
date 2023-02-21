package com.android.core.retrofit.impl

import retrofit2.Call
import retrofit2.http.GET


/**
 * Test api.
 */
internal interface TestApi {
    @GET("/test")
    fun test(): Call<TestData>
}

/**
 * Test data.
 *
 * @property name Some property
 * @constructor Create [TestData]
 */
internal data class TestData(val name: String)