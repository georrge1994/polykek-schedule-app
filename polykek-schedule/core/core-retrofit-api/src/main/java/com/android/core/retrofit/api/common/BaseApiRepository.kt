package com.android.core.retrofit.api.common

import com.android.common.models.api.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Base api repository.
 *
 * @constructor Create [BaseApiRepository]
 */
abstract class BaseApiRepository {
    /**
     * Safe api call.
     *
     * @param T Type of response
     * @param apiToBeCalled Api to be called
     * @return [Resource]
     */
    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): Resource<T> = withContext(Dispatchers.IO) {
        try {
            val response: Response<T> = apiToBeCalled()
            if (response.isSuccessful) {
                Resource.Success(data = response.body()!!)
            } else {
                Resource.Error(errorMessage = response.message() ?: "Something went wrong")
            }
        } catch (e: HttpException) {
            Resource.Error(errorMessage = e.message ?: "Please check your network connection")
        } catch (e: IOException) {
            Resource.Error(errorMessage = e.message ?: "Something went wrong")
        } catch (e: Exception) {
            Resource.Error(errorMessage = "Something went wrong")
        }
    }
}