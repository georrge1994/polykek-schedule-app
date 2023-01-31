package com.android.feature.schedule.professor.adapters

import androidx.recyclerview.widget.RecyclerView

/**
 * Professor schedule header item view holder.
 *
 * @property professorHeaderItemView [ProfessorHeaderItemView]
 * @constructor Create [ProfessorScheduleHeaderItemViewHolder]
 */
internal class ProfessorScheduleHeaderItemViewHolder(
    private val professorHeaderItemView: ProfessorHeaderItemView
) : RecyclerView.ViewHolder(professorHeaderItemView) {
    /**
     * Bind to.
     *
     * @param title Title
     */
    internal fun bindTo(title: String) {
        professorHeaderItemView.text = title
    }
}