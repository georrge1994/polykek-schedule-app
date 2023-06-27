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
     */
    internal fun bindTo(faq: FAQ) = with(faqItemView) {
        question.text = context.getString(faq.questionId)
        answer.text = context.getString(faq.answerId)
        answer.isVisible = faq.isOpened
        moreIcon.isVisibleOrInvisible(!faq.isOpened)
        parentLayout.setOnClickListener { listener.onClick(bindingAdapterPosition, !faq.isOpened) }
    }
}