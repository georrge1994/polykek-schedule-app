package com.android.professors.list.mvi

import com.android.common.models.professors.Professor
import com.android.core.ui.mvi.MviIntent

/**
 * Professor intent.
 *
 * @constructor Create empty constructor for professor intent
 */
internal sealed class ProfessorIntent : MviIntent {
    /**
     * Open professor search screen.
     */
    internal object OpenProfessorSearchScreen : ProfessorIntent()

    /**
     * Open faq screen.
     */
    internal object OpenFAQScreen : ProfessorIntent()

    /**
     * Open professor schedule screen.
     *
     * @property professor Professor pojo
     * @constructor Create [OpenProfessorScheduleScreen]
     */
    internal data class OpenProfessorScheduleScreen(val professor: Professor) : ProfessorIntent()
}