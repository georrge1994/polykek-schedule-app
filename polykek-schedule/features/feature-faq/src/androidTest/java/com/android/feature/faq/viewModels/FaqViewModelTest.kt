package com.android.feature.faq.viewModels

import com.android.feature.faq.R
import com.android.feature.faq.models.FAQ
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Faq view model test for [FaqViewModel].
 *
 * @constructor Create empty constructor for faq view model test
 */
class FaqViewModelTest : BaseViewModelUnitTest() {
    private val faqViewModel = FaqViewModel()

    /**
     * Get faq items test.
     */
    @Test
    fun getFaqItemsTest() {
        listOf(
            FAQ(R.string.faq_fragment_question_1_title, R.string.faq_fragment_question_1_description),
            FAQ(R.string.faq_fragment_question_2_title, R.string.faq_fragment_question_2_description),
            FAQ(R.string.faq_fragment_question_3_title, R.string.faq_fragment_question_3_description),
            FAQ(R.string.faq_fragment_question_4_title, R.string.faq_fragment_question_4_description),
            FAQ(R.string.faq_fragment_question_5_title, R.string.faq_fragment_question_5_description),
            FAQ(R.string.faq_fragment_question_6_title, R.string.faq_fragment_question_6_description)
        ).apply {
            assertEquals(this, faqViewModel.getFaqItems())
        }
    }
}