package com.android.shared.code.utils.general

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Shared preference utils provides work wit shared preference memory.
 *
 * @property application Application
 * @constructor Create [SharedPreferenceUtils]
 */
@Singleton
class SharedPreferenceUtils @Inject constructor(private val application: Application) {
    private val sharedPreferences: SharedPreferences
        get() = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)

    /**
     * Get double value from shared preference memory.
     *
     * @param key Key
     * @param defaultValue Default value
     * @return Value from memory
     */
    fun getDouble(key: String, defaultValue: String = "0"): Double = getString(key, defaultValue).toDouble()

    /**
     * Get int value from shared preference memory.
     *
     * @param key Key
     * @param defaultValue Default value
     * @return Value from memory
     */
    fun getInt(key: String, defaultValue: Int = 0): Int {
        val share = sharedPreferences

        try {
            return share.getInt(key, defaultValue)
        } catch (e: Exception) {
        }

        try {
            val string = share.getString(key, defaultValue.toString()) ?: defaultValue.toString()
            return string.toInt()
        } catch (e: Exception) {
        }

        return defaultValue
    }

    /**
     * Get long value from shared preference memory.
     *
     * @param key Key
     * @param defaultValue Default value
     * @return Value from memory
     */
    fun getLong(key: String, defaultValue: Long = 0): Long {
        val share = sharedPreferences

        try {
            return share.getLong(key, defaultValue)
        } catch (e: Exception) {
        }

        try {
            val string = share.getString(key, defaultValue.toString()) ?: defaultValue.toString()
            return string.toLong()
        } catch (e: Exception) {
        }

        return defaultValue
    }

    /**
     * Get boolean value from shared preference memory.
     *
     * @param key Key
     * @param defaultValue Default value
     * @return Value from memory
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    /**
     * Get string value from shared preference memory.
     *
     * @param key Key
     * @param defaultValue Default value
     * @return Value from memory
     */
    fun getString(key: String, defaultValue: String = ""): String =
        sharedPreferences.getString(key, defaultValue) ?: defaultValue

    /**
     * Add value to shared preference memory.
     *
     * @param key Key
     * @param value Any value
     */
    fun add(key: String, value: Any) {
        sharedPreferences.edit().apply {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Boolean -> putBoolean(key, value)
                is Double -> putString(key, value.toString())
                else -> putString(key, value.toString())
            }
            apply()
        }
    }

    /**
     * Contains key in shared memory.
     *
     * @param key Key
     * @return Is contains flag
     */
    fun contains(key: String): Boolean = sharedPreferences.contains(key)

    /**
     * Remove value from shared memory by key.
     *
     * @param key Key
     */
    fun remove(key: String) = sharedPreferences.edit().remove(key).apply()
}