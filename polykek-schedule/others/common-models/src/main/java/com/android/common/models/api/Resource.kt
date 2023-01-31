package com.android.common.models.api

/**
 * Resource.
 *
 * @param T Type of response data
 * @property data Response data
 * @property message Error message
 * @constructor Create [Resource]
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * We'll wrap our data in this 'Success' class in case of success response from api.
     *
     * @param T Type of response
     * @constructor Create [Success]
     *
     * @param data Response
     */
    class Success<T>(data: T) : Resource<T>(data = data)

    /**
     * We'll pass error message wrapped in this 'Error' class to the UI in case of failure response.
     *
     * @param T Type of response
     * @constructor Create [Error]
     *
     * @param errorMessage Text of error
     */
    class Error<T>(errorMessage: String) : Resource<T>(message = errorMessage)
}