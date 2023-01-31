package com.android.feature.schools.adapters

import androidx.recyclerview.widget.RecyclerView
import com.android.feature.schools.models.School

/**
 * School view holder.
 *
 * @property schoolItemView [SchoolItemView]
 * @property listener Item action listener
 * @constructor Create [SchoolViewHolder]
 */
internal class SchoolViewHolder(
    private val schoolItemView: SchoolItemView,
    private val listener: ISchoolActions
) : RecyclerView.ViewHolder(schoolItemView) {
    /**
     * Bind to.
     *
     * @param school School
     */
    internal fun bindTo(school: School) = with(school) {
        schoolItemView.text = name
        schoolItemView.setOnClickListener { listener.onClick(this) }
    }
}