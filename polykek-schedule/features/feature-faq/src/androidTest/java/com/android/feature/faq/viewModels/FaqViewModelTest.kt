package com.android.feature.faq.viewModels

import com.android.feature.faq.R
import com.android.feature.faq.models.FAQ
import com.android.feature.faq.mvi.FaqAction
import com.android.feature.faq.mvi.FaqIntent
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
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
    fun getFaqItemsTest() = runBlockingUnit {
        listOf(
            FAQ(R.string.faq_fragment_question_1_title, R.string.faq_fragment_question_1_description),
            FAQ(R.string.faq_fragment_question_2_title, R.string.faq_fragment_question_2_description),
            FAQ(R.string.faq_fragment_question_3_title, R.string.faq_fragment_question_3_description),
            FAQ(R.string.faq_fragment_question_4_title, R.string.faq_fragment_question_4_description),
            FAQ(R.string.faq_fragment_question_5_title, R.string.faq_fragment_question_5_description),
            FAQ(R.string.faq_fragment_question_6_title, R.string.faq_fragment_question_6_description)
        ).apply {
            assertEquals(this, faqViewModel.state.value.items)
        }
    }

    /**
     * Get faq items test selection.
     */
    @Test
    fun getFaqItemsTest_selection() = runBlockingUnit {
        listOf(
            FAQ(R.string.faq_fragment_question_1_title, R.string.faq_fragment_question_1_description, true),
            FAQ(R.string.faq_fragment_question_2_title, R.string.faq_fragment_question_2_description),
            FAQ(R.string.faq_fragment_question_3_title, R.string.faq_fragment_question_3_description),
            FAQ(R.string.faq_fragment_question_4_title, R.string.faq_fragment_question_4_description),
            FAQ(R.string.faq_fragment_question_5_title, R.string.faq_fragment_question_5_description),
            FAQ(R.string.faq_fragment_question_6_title, R.string.faq_fragment_question_6_description)
        ).apply {
            faqViewModel.dispatchIntentAsync(FaqIntent.ClickByItem(0, true)).joinWithTimeout()
            assertEquals(this, faqViewModel.state.value.items)
        }
    }

    /**
     * Check feedback action.
     */
    @Test
    fun checkFeedbackAction() = runBlockingUnit {
        faqViewModel.dispatchIntentAsync(FaqIntent.OpenFeedback).joinWithTimeout()
        faqViewModel.action.subscribeAndCompareFirstValue(FaqAction.OpenFeedback)
    }
}