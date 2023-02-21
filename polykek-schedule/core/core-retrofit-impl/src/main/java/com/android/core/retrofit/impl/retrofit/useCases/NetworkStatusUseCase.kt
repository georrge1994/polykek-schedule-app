package com.android.core.retrofit.impl.retrofit.useCases

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject

/**
 * Provides actual status of network.
 *
 * @property application Application object to get context
 * @constructor Create [NetworkStatusUseCase]
 */
internal class NetworkStatusUseCase @Inject constructor(private val application: Application) : IUseCase {
    /**
     * Is network available.
     *
     * A couple words about try-catch: once happen crash:
     * "Fatal Exception: java.lang.SecurityException: Package android does not belong to 10353".
     * Sounds like a native problem, but anyway will try to avoid it.
     *
     * @return Condition result
     */
    internal fun isNetworkAvailable(): Boolean = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            application.isNetworkAvailableSDK23()
        } else {
            application.isNetworkAvailableDeprecated()
        }
    } catch (e: Exception) {
        false
    }

    /**
     * Is network available sdk 23.
     *
     * @receiver [Context]
     * @return Condition result
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun Context.isNetworkAvailableSDK23(): Boolean {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val activeNetwork = connectivityManager?.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    /**
     * Is network available for API < 23.
     *
     * @receiver [Context]
     * @return Condition result
     */
    @Suppress("DEPRECATION")
    private fun Context.isNetworkAvailableDeprecated(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // If no network is available networkInfo will be null, otherwise check if we are connected.
        try {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }
}