package com.android.shared.code.utils.general

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

val ioScope: CoroutineScope
    get() = CoroutineScope(Dispatchers.IO + Job())

/**
 * Shortcut function to avoid ?: 0 writing everywhere.
 *
 * @receiver [T] or null
 * @param T Type of number
 * @return [T]
 */
inline fun <reified T : Number> T?.getZeroIfNull(): T = (this ?: 0) as T