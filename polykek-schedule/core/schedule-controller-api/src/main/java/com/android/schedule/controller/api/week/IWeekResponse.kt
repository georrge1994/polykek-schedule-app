package com.android.schedule.controller.api.week

/**
 * Response result week.
 */
interface IWeekResponse {
    val name: String
    val weekInfo: WeekInfoResponse
    val days: List<DayResponse>
}