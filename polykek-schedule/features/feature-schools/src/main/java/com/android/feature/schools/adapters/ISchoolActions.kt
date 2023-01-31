package com.android.feature.schools.adapters

import com.android.feature.schools.models.School

/**
 * School actions in recycler view.
 */
internal interface ISchoolActions {
    /**
     * On click by [School].
     *
     * @param school School
     */
    fun onClick(school: School)
}