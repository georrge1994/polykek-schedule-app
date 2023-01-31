package com.android.feature.schools.adapters

import android.content.Context
import android.view.ViewGroup
import com.android.core.ui.adapters.BaseRecyclerViewAdapter
import com.android.feature.schools.models.School

/**
 * Schools recycler view adapter.
 *
 * @property context Context
 * @property schoolClickListener [ISchoolActions]
 * @constructor Create [SchoolsRecyclerViewAdapter]
 */
internal class SchoolsRecyclerViewAdapter(
    private val context: Context,
    private val schoolClickListener: ISchoolActions
) : BaseRecyclerViewAdapter<SchoolViewHolder, School>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SchoolViewHolder(SchoolItemView(context), schoolClickListener)

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) =
        holder.bindTo(items[position])
}
