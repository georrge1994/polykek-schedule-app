package com.android.schedule.controller.api

import com.android.common.models.savedItems.SavedItem
import com.android.common.models.schedule.Week
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Schedule controller interface.
 */
interface IScheduleController {
    /**
     * Selected group or professor.
     */
    val selectedItem: SavedItem?

    /**
     * Selected day on the schedule screen.
     */
    var indexOfDay: Int

    /**
     * Current week schedule in the flow format.
     */
    val weekFlow: MutableStateFlow<Week?>

    /**
     * Update schedule for new period.
     *
     * @param period Period
     */
    suspend fun updateSchedule(period: String)
}