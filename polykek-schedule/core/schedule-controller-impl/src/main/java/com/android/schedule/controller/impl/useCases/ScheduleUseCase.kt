package com.android.schedule.controller.impl.useCases

import com.android.common.models.savedItems.SavedItem
import com.android.common.models.schedule.Week
import com.android.core.retrofit.api.common.CatchResourceUseCase
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.schedule.controller.api.week.IWeekResponse
import com.android.schedule.controller.impl.api.ScheduleApiRepository
import com.android.shared.code.utils.general.isToday
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * This class fetches schedule and covert it to app format.
 *
 * @property scheduleApiRepository Api repository for student and professor
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @property scheduleResponseUseCase Converts response-week format to UI-week
 * @property harryPotterJokerUseCase April 1st joker
 * @constructor Create [ScheduleUseCase]
 *
 * @param backgroundMessageBus Common background message bus with snack-bar output
 */
internal class ScheduleUseCase @Inject constructor(
    private val scheduleApiRepository: ScheduleApiRepository,
    private val savedItemsRoomRepository: ISavedItemsRoomRepository,
    private val scheduleResponseUseCase: ScheduleResponseConvertUseCase,
    private val harryPotterJokerUseCase: HarryPotterJokerUseCase,
    @Named(BACKGROUND_MESSAGE_BUS) backgroundMessageBus: MutableSharedFlow<String>
) : CatchResourceUseCase(backgroundMessageBus) {
    private var cachedSelectedItem: SavedItem? = null
    private var cachedGroupPeriod: String? = null
    private var cachedWeek: Week? = null

    /**
     * Get schedule for saved item. This method is used for main week schedule screen.
     *
     * @param selectedItem Selected item
     * @param period Period
     * @return [Week] or null
     */
    internal suspend fun getSchedule(
        selectedItem: SavedItem,
        period: String
    ): Week? = if (selectedItem == cachedSelectedItem && period == cachedGroupPeriod && cachedWeek != null) {
        cachedWeek
    } else {
        if (selectedItem.isGroup) {
            scheduleApiRepository.getScheduleForGroup(selectedItem.id, period)
        } else {
            scheduleApiRepository.getProfessorSchedule(selectedItem.id, period)
        }.catchRequestError { weekResponse ->
            checkFirstApril(selectedItem.id, weekResponse)?.let { changedRequestResultWeek ->
                this.cachedSelectedItem = selectedItem
                this.cachedGroupPeriod = period
                this.cachedWeek = scheduleResponseUseCase.convertToWeekFormat(changedRequestResultWeek, selectedItem.id)
                checkAndUpdateNameIfNeed(selectedItem.name, weekResponse.name)
                cachedWeek
            }
        }
    }

    /**
     * Check April 1st joke if it is.
     *
     * @param groupId Group id
     * @param requestResultWeek Request result week
     * @return [IWeekResponse] or null
     */
    private fun checkFirstApril(
        groupId: Int,
        requestResultWeek: IWeekResponse?
    ): IWeekResponse? = if (isToday(Calendar.APRIL, 1)) {
        harryPotterJokerUseCase.replaceLessonsWithHarryPotterMemes(groupId, requestResultWeek)
    } else {
        requestResultWeek
    }

    /**
     * Time to time Polytech updates names of groups and professors. We need to check it.
     *
     * @param currentName Name of group or professor
     * @param justReceivedName Just received name of group or professor
     */
    private suspend fun checkAndUpdateNameIfNeed(currentName: String, justReceivedName: String) {
        if (justReceivedName.isNotBlank() && justReceivedName != currentName) {
            savedItemsRoomRepository.saveItemAndSelectIt(cachedSelectedItem!!.copy(name = justReceivedName))
        }
    }
}