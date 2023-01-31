package com.android.feature.schedule.base.models

/**
 * Information about date for date-picker.
 *
 * @property day Day
 * @property month Month
 * @property year Year
 * @constructor Create [DatePickerInfo]
 */
internal data class DatePickerInfo(
    val day: Int,
    val month: Int,
    val year: Int
)