package com.android.schedule.controller.impl.useCases

import android.app.Application
import com.android.common.models.professors.Professor
import com.android.common.models.schedule.Day
import com.android.common.models.schedule.Lesson
import com.android.common.models.schedule.Week
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.schedule.controller.api.ITeacherToProfessorConvertor
import com.android.schedule.controller.api.week.DayResponse
import com.android.schedule.controller.api.week.IWeekResponse
import com.android.schedule.controller.api.week.lesson.LessonResponse
import com.android.schedule.controller.api.week.lesson.TeacherResponse
import com.android.schedule.controller.impl.R
import com.android.schedule.controller.impl.api.ProfessorWeekResponse
import com.android.shared.code.utils.markers.IUseCase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private const val TITLE_FORMAT = "MMM d, EEE"

/**
 * Schedule response convert use case.
 *
 * @property application Application object to get context
 * @property notesRoomRepository Notes room repository needs to set hasNoteFlag
 * @property lessonConvertUseCase Convert [LessonResponse] to [Lesson]
 * @constructor Create [ScheduleResponseConvertUseCase]
 */
@Singleton
internal class ScheduleResponseConvertUseCase @Inject constructor(
    private val application: Application,
    private val notesRoomRepository: INotesRoomRepository,
    private val lessonConvertUseCase: LessonConvertFormatUseCase
) : IUseCase, ITeacherToProfessorConvertor {
    private val defaultFormat = SimpleDateFormat(PERIOD_FORMAT, Locale.ENGLISH)
    private val desiredFormat = SimpleDateFormat(TITLE_FORMAT, Locale.ENGLISH)

    /**
     * Convert [ProfessorWeekResponse] to [Week] format.
     *
     * @param professorWeekResponse [ProfessorWeekResponse]
     * @return [Week]
     */
    suspend fun convertToWeekFormat(professorWeekResponse: ProfessorWeekResponse): Week = with(professorWeekResponse) {
        val noteIds = notesRoomRepository.getNoteIds().toSet()
        Week(getTitle(), days.getFormattedDaysMap(noteIds, null))
    }

    /**
     * Convert to week format.
     *
     * @param weekResponse [IWeekResponse]
     * @param itemId Item id
     * @return [Week] or null
     */
    suspend fun convertToWeekFormat(weekResponse: IWeekResponse, itemId: Int): Week = with(weekResponse) {
        val noteIds = notesRoomRepository.getNoteIds().toSet()
        Week(getTitle(), days.getFormattedDaysMap(noteIds, itemId))
    }

    /**
     * Convert [TeacherResponse] to [Professor].
     *
     * @param teacherResponse [TeacherResponse]
     * @return [Professor] or null
     */
    override fun convertTeacherToProfessor(teacherResponse: TeacherResponse): Professor? =
        lessonConvertUseCase.convertTeacherToProfessor(teacherResponse)

    /**
     * Get title.
     *
     * @receiver [IWeekResponse]
     * @return Week title
     */
    private fun IWeekResponse.getTitle() = if (weekInfo.isOdd) {
        application.getString(R.string.schedule_fragment_odd_week)
    } else {
        application.getString(R.string.schedule_fragment_even_week)
    }

    /**
     * Get formatted days map.
     *
     * @receiver List of days from response object
     * @param noteIds Set of all note ids
     * @param groupId Group id
     * @return Day map with lessons
     */
    private fun List<DayResponse>.getFormattedDaysMap(noteIds: Set<String>, groupId: Int?): Map<Int, Day> = HashMap<Int, Day>().apply {
        this@getFormattedDaysMap.forEach {
            this[it.weekday - 1] = Day(
                date = it.getDayTitle(),
                lessons = lessonConvertUseCase.convertToLessonFormat(it.lessons, noteIds, groupId)
            )
        }
    }

    /**
     * Get day title.
     *
     * @receiver [DayResponse]
     * @return Current date in text format
     */
    private fun DayResponse.getDayTitle(): String = try {
        val checkedDate = date ?: ""
        val date = defaultFormat.parse(checkedDate) ?: ""
        desiredFormat.format(date)
    } catch (parseException: ParseException) {
        ""
    }
}