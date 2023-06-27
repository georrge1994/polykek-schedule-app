package com.android.professors.search.mvi

import com.android.common.models.professors.Professor
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.mvi.MviAction

/**
 * Professors search action.
 *
 * @constructor Create empty constructor for professors search action
 */
internal sealed class ProfessorsSearchAction : MviAction {
    /**
     * Show the next screen.
     *
     * @property scheduleMode The mode of screen
     * @property professor The professor
     * @constructor Create [ShowNextScreen]
     */
    internal data class ShowNextScreen(
        val scheduleMode: ScheduleMode,
        val professor: Professor
    ) : ProfessorsSearchAction()
}