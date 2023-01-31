package com.android.feature.faq.adapters

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.core.ui.view.ext.isVisibleOrInvisible
import com.android.feature.faq.models.FAQ

/**
 * FAQ view holder.
 *
 * @property faqItemView [FaqItemView]
 * @property listener Item action listener
 * @constructor Create [FaqViewHolder]
 */
internal class FaqViewHolder constructor(
    private val faqItemView: FaqItemView,
    private val listener: ItemClickListener
) : RecyclerView.ViewHolder(faqItemView.parentLayout) {
    /**
     * Bind data to view.
     *
     * @param faq [FAQ]
     * @param isSelectedItem Is selected item
     */
    internal fun bindTo(faq: FAQ, isSelectedItem: Boolean) = with(faqItemView) {
        question.text = context.getString(faq.questionId)
        answer.text = context.getString(faq.answerId)
        answer.isVisible = isSelectedItem
        moreIcon.isVisibleOrInvisible(!isSelectedItem)
        parentLayout.setOnClickListener { listener.onClick(bindingAdapterPosition) }
    }
}