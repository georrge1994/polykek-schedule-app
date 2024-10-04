package com.android.professors.list.mvi

import com.android.common.models.professors.Professor
import com.android.core.ui.mvi.MviAction

/**
 * Professor action.
 *
 * @constructor Create empty constructor for professor action
 */
internal sealed class ProfessorAction : MviAction {
    /**
     * Open professor search screen.
     */
    internal data object OpenProfessorSearchScreen : ProfessorAction()

    /**
     * Open faq screen.
     */
    internal data object OpenFAQScreen : ProfessorAction()

    /**
     * Open professor schedule screen.
     *
     * @property professor Professor
     * @constructor Create [OpenProfessorScheduleScreen]
     */
    internal data class OpenProfessorScheduleScreen(val professor: Professor) : ProfessorAction()
}