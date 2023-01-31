package com.android.schedule.controller.impl.useCases

import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.shared.code.utils.general.toCalendar
import com.android.shared.code.utils.markers.IUseCase
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

const val PERIOD_FORMAT = "yyyy-MM-dd"

/**
 * This class is used to work with date on the schedule screen.
 *
 * @constructor Create empty constructor for schedule date use case
 */
@Singleton
internal class ScheduleDateUseCase @Inject constructor() : IScheduleDateUseCase, IUseCase {
    private val periodFormat = SimpleDateFormat(PERIOD_FORMAT, Locale.ENGLISH)

    override var selectedDate: Calendar = getCurrentDay().toCalendar().correctSunday()
        private set
        @Synchronized get

    @Synchronized
    override fun getPeriod(): String = periodFormat.format(selectedDate.time)

    @Synchronized
    override fun setSelectedDay(year: Int, month: Int, day: Int) {
        selectedDate.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DATE, day)
        }.correctSunday()
    }

    @Synchronized
    override fun addToSelectedDate(field: Int, amount: Int) = selectedDate.add(field, amount)

    @Synchronized
    override fun getCurrentDay(): Date = if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.AUGUST) {
        val cal = Date().toCalendar()
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.time
    } else {
        Date()
    }

    /**
     * If date is Sunday, we have to show the next Monday.
     *
     * @receiver [Calendar]
     * @return [Calendar]
     */
    private fun Calendar.correctSunday(): Calendar {
        if (get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            add(Calendar.DATE, 1)
        return this
    }
}