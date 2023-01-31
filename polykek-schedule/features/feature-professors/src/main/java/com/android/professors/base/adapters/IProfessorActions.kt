package com.android.professors.base.adapters

import com.android.common.models.professors.Professor

/**
 * Professor actions.
 */
internal interface IProfessorActions {
    /**
     * On click.
     *
     * @param professor Professor
     */
    fun onClick(professor: Professor)
}