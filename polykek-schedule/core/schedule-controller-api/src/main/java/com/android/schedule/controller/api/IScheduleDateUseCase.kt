package com.android.schedule.controller.api

import java.util.*

/**
 * Provides controls for current date. One date for all screens.
 */
interface IScheduleDateUseCase {
    val selectedDate: Calendar

    /**
     * Get period for requests.
     *
     * @return Date in the specific format
     */
    fun getPeriod(): String

    /**
     * Set specific date to [selectedDate].
     *
     * @param year Year
     * @param month Month
     * @param day Day
     */
    fun setSelectedDay(year: Int, month: Int, day: Int)

    /**
     * Add to selected date.
     *
     * @param field Field
     * @param amount Amount
     */
    fun addToSelectedDate(field: Int, amount: Int)

    /**
     * Get today OR 1st September for August (August is a last vacation month before a new school year).
     *
     * @return [Date]
     */
    fun getCurrentDay(): Date
}