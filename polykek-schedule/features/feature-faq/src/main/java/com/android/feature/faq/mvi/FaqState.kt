package com.android.feature.faq.mvi

import com.android.core.ui.mvi.MviState
import com.android.feature.faq.R
import com.android.feature.faq.models.FAQ

/**
 * Faq state properties.
 */
internal interface FaqStateProperties {
    val items: List<FAQ>
}

/**
 * Faq state.
 *
 * @property items [FAQ] items
 * @constructor Create [FaqState]
 */
internal sealed class FaqState : MviState, FaqStateProperties {
    /**
     * Default state - all items are closed.
     */
    internal object Default : FaqState() {
        override val items: List<FAQ> = listOf(
            FAQ(R.string.faq_fragment_question_1_title, R.string.faq_fragment_question_1_description),
            FAQ(R.string.faq_fragment_question_2_title, R.string.faq_fragment_question_2_description),
            FAQ(R.string.faq_fragment_question_3_title, R.string.faq_fragment_question_3_description),
            FAQ(R.string.faq_fragment_question_4_title, R.string.faq_fragment_question_4_description),
            FAQ(R.string.faq_fragment_question_5_title, R.string.faq_fragment_question_5_description),
            FAQ(R.string.faq_fragment_question_6_title, R.string.faq_fragment_question_6_description)
        )
    }

    /**
     * New selection.
     *
     * @constructor Create [Update]
     *
     * @param items New items
     */
    internal data class Update(override val items: List<FAQ>) : FaqState()
}