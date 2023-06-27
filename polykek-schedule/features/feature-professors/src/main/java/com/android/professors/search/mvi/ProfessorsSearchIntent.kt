package com.android.professors.search.mvi

import com.android.common.models.professors.Professor
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.mvi.MviIntent

/**
 * Professors search intent.
 *
 * @constructor Create empty constructor for professors search intent
 */
internal sealed class ProfessorsSearchIntent : MviIntent {
    /**
     * Init content.
     *
     * @property scheduleMode The mode of screen
     * @constructor Create [InitContent]
     */
    internal data class InitContent(val scheduleMode: ScheduleMode) : ProfessorsSearchIntent()

    /**
     * Search professors by keyword.
     *
     * @property keyword The keyword
     * @constructor Create [SearchProfessorsByKeyword]
     */
    internal data class SearchProfessorsByKeyword(val keyword: String) : ProfessorsSearchIntent()

    /**
     * Save and show next screen.
     *
     * @property professor The professor
     * @constructor Create [SaveAndShowNextScreen]
     */
    internal data class SaveAndShowNextScreen(val professor: Professor) : ProfessorsSearchIntent()
}