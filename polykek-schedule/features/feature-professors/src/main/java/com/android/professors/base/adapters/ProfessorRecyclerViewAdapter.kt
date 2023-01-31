package com.android.professors.base.adapters

import android.content.Context
import android.view.ViewGroup
import com.android.common.models.professors.Professor
import com.android.core.ui.adapters.BaseRecyclerViewAdapter

/**
 * Professor recycler view adapter.
 *
 * @property context Context
 * @property listener Item action listener
 * @constructor Create [ProfessorRecyclerViewAdapter]
 * @author darkt on 6/7/2020.
 */
internal class ProfessorRecyclerViewAdapter(
    private val context: Context,
    private var listener: IProfessorActions
) : BaseRecyclerViewAdapter<ProfessorViewHolder, Professor>() {
    override fun onBindViewHolder(viewHolder: ProfessorViewHolder, position: Int) =
        viewHolder.bindTo(items[position])

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ProfessorViewHolder =
        ProfessorViewHolder(ProfessorItemView(context), listener)
}