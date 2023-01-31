package com.android.feature.schedule.student.adapters.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.common.models.schedule.Lesson
import com.android.core.ui.adapters.BaseRecyclerViewAdapter
import com.android.core.ui.view.ext.setDivider
import com.android.feature.schedule.R
import com.android.feature.schedule.base.adapters.ILessonActions
import com.android.feature.schedule.base.adapters.LessonViewHolder

/**
 * Lessons recycler view adapter.
 *
 * @property context Context
 * @property listener Item action listener
 * @constructor Create [LessonsRecyclerViewAdapter]
 * @author darkt on 1/9/2018
 */
internal class LessonsRecyclerViewAdapter(
    private val context: Context,
    private val listener: ILessonActions
) : BaseRecyclerViewAdapter<LessonViewHolder, Lesson>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.setDivider(R.drawable.item_divider)
    }

    override fun onBindViewHolder(viewHolder: LessonViewHolder, position: Int) {
        viewHolder.bindTo(items[position])
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LessonViewHolder =
        LessonViewHolder.onCreate(context, listener)
}