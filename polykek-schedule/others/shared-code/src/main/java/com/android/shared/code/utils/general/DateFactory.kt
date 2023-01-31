package com.android.shared.code.utils.general

import java.util.*
import javax.inject.Inject

/**
 * Date factory to make testing more simple.
 *
 * @constructor Create empty constructor for date factory
 */
class DateFactory @Inject constructor() {
    /**
     * Get today.
     *
     * @return [Date]
     */
    fun getToday() = Date()
}