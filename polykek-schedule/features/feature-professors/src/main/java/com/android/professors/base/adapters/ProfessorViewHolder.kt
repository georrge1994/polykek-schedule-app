package com.android.professors.base.adapters

import androidx.recyclerview.widget.RecyclerView
import com.android.common.models.professors.Professor

/**
 * Professor view holder.
 *
 * @property professorItemView [ProfessorItemView]
 * @property listener Item action listener
 * @constructor Create [ProfessorViewHolder]
 */
internal class ProfessorViewHolder(
    private val professorItemView: ProfessorItemView,
    private val listener: IProfessorActions
) : RecyclerView.ViewHolder(professorItemView) {
    /**
     * Bind to.
     *
     * @param professor Professor
     */
    internal fun bindTo(professor: Professor) = with(professor) {
        professorItemView.name.text = fullName
        professorItemView.chair.text = chair
        professorItemView.setOnClickListener { listener.onClick(this) }
    }
}