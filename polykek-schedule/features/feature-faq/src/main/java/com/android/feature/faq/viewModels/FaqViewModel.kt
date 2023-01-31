package com.android.feature.faq.viewModels

import com.android.core.ui.viewModels.BaseViewModel
import com.android.feature.faq.R
import com.android.feature.faq.models.FAQ
import javax.inject.Inject

/**
 * Frequently Asked Question view model.
 *
 * @constructor Create empty constructor for faq view model
 */
internal class FaqViewModel @Inject constructor() : BaseViewModel() {
    /**
     * Get faq items.
     *
     * @return List of [FAQ]
     */
    internal fun getFaqItems() = listOf(
        FAQ(R.string.faq_fragment_question_1_title, R.string.faq_fragment_question_1_description),
        FAQ(R.string.faq_fragment_question_2_title, R.string.faq_fragment_question_2_description),
        FAQ(R.string.faq_fragment_question_3_title, R.string.faq_fragment_question_3_description),
        FAQ(R.string.faq_fragment_question_4_title, R.string.faq_fragment_question_4_description),
        FAQ(R.string.faq_fragment_question_5_title, R.string.faq_fragment_question_5_description),
        FAQ(R.string.faq_fragment_question_6_title, R.string.faq_fragment_question_6_description)
    )
}