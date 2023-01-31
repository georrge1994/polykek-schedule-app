package com.android.schedule.controller.impl

import com.android.common.models.savedItems.SavedItem
import com.android.common.models.schedule.Week
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.impl.useCases.ScheduleDateUseCase
import com.android.schedule.controller.impl.useCases.ScheduleUseCase
import com.android.shared.code.utils.general.ioScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This is a schedule controller. It allows to synchronize UI changes between all tabs and avoid superfluous request, code duplicates.
 *
 * @property scheduleUseCase This class fetches schedule and covert it to app format
 * @property notesRoomRepository Notes room repository needs to update flags on saved item
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @property scheduleDateUseCase Use case to work with schedule dates
 * @constructor Create [ScheduleController]
 */
@Singleton
internal class ScheduleController @Inject constructor(
    private val scheduleUseCase: ScheduleUseCase,
    private val notesRoomRepository: INotesRoomRepository,
    private val savedItemsRoomRepository: ISavedItemsRoomRepository,
    private val scheduleDateUseCase: ScheduleDateUseCase
) : IScheduleController {
    private var scheduleRequestJob: Job? = null

    // Synchronize all async requests to data by mutex.
    private val mutex = Mutex()

    @Volatile
    override var selectedItem: SavedItem? = null
        private set

    @Volatile
    private var week: Week? = null

    @Volatile
    override var indexOfDay: Int = 0 // [0..6].

    override val weekFlow = MutableStateFlow<Week?>(null)

    init {
        subscribeToSelectedItem()
        subscribeToNoteChanges()
    }

    /**
     * Infinite subscription to selected item (group or professor object).
     */
    private fun subscribeToSelectedItem() = savedItemsRoomRepository.getSelectedItemFlow()
        .filterNotNull()
        .onEach { item ->
            selectedItem = item
            sendRequestAndUpdateWeek { scheduleUseCase.getSchedule(item, scheduleDateUseCase.getPeriod()) }
        }.flowOn(Dispatchers.IO)
        .launchIn(ioScope)

    /**
     * Subscribe to note changes. Every time when user create, remove or edit
     */
    private fun subscribeToNoteChanges() = notesRoomRepository.getNoteIdsFlow()
        .onEach { ids ->
            updateNotes(ids?.toSet() ?: emptySet())
        }.flowOn(Dispatchers.IO)
        .launchIn(ioScope)

    /**
     * Update schedule for new period.
     *
     * @param period Period
     */
    override suspend fun updateSchedule(period: String) {
        val item = selectedItem ?: return
        sendRequestAndUpdateWeek { scheduleUseCase.getSchedule(item, period) }
    }

    /**
     * Update note-flags for current week lessons.
     *
     * @param noteIds Ids of exist notes.
     */
    private suspend fun updateNotes(noteIds: Set<String>) = mutex.withLock {
        var wasChanged = false
        week?.days?.values?.forEach { day ->
            day.lessons.forEach { lesson ->
                if (lesson.withNotes != noteIds.contains(lesson.noteId))
                    wasChanged = true
                lesson.withNotes = noteIds.contains(lesson.noteId)
            }
        }
        // Refresh data for subscribers if changes were done.
        if (wasChanged)
            weekFlow.emit(week)
    }

    /**
     * Cancel current request, if it exists. Start new job, invoke request and safety update week object.
     *
     * @param requestAction Request action
     */
    private suspend fun sendRequestAndUpdateWeek(requestAction: suspend () -> Week?) {
        scheduleRequestJob?.cancel()
        scheduleRequestJob = ioScope.launch {
            val weekLocal = requestAction.invoke()
            // Safety and quick update.
            mutex.withLock {
                week = weekLocal
                weekFlow.emit(week)
            }
        }.also { it.join() }
    }
}