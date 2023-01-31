package com.android.shared.code.utils.general

import java.util.*

/**
 * Compare date with today.
 *
 * @param month Month
 * @param day Day
 * @return True if [month] and [day] are today. Don't check the year, because app behaviour doesn't change between years.
 */
fun isToday(month: Int, day: Int): Boolean {
    val calendar = Date().toCalendar()
    return calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DATE) == day
}

/**
 * [Date] to calendar instance.
 *
 * @receiver [Date]
 * @return [Calendar]
 */
fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}