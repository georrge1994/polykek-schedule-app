package com.android.feature.schedule.professor.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.common.models.schedule.Lesson
import com.android.core.ui.adapters.BaseRecyclerViewAdapter
import com.android.feature.schedule.base.adapters.LessonViewHolder

private const val TITLE_TYPE = 0
private const val LESSON_TYPE = 1

/**
 * Professor lessons recycler view adapter.
 *
 * @property context Context
 * @constructor Create [ProfessorLessonsRecyclerViewAdapter]
 * @author darkt on 6/7/2020
 */
internal class ProfessorLessonsRecyclerViewAdapter(private val context: Context) : BaseRecyclerViewAdapter<RecyclerView.ViewHolder, Any>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TITLE_TYPE -> (holder as ProfessorScheduleHeaderItemViewHolder).bindTo(items[position] as String)
            else -> (holder as LessonViewHolder).bindTo(items[position] as Lesson)
        }
    }

    override fun getItemViewType(position: Int) = if (items[position] is String)
        TITLE_TYPE
    else
        LESSON_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TITLE_TYPE -> ProfessorScheduleHeaderItemViewHolder(ProfessorHeaderItemView(context))
        else -> LessonViewHolder.onCreate(context, null)
    }
}