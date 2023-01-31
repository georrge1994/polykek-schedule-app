package com.android.feature.feedback.viewModels

import com.android.feature.feedback.models.FeedbackType
import com.android.feature.feedback.useCases.FeedbackUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.collectPost
import com.android.test.support.androidTest.utils.getOrAwaitValue
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Feedback view model test.
 *
 * @constructor Create empty constructor for feedback view model test
 */
class FeedbackViewModelTest : BaseViewModelUnitTest() {
    private val feedbackUseCase: FeedbackUseCase = mockk()
    private val feedbackViewModel = FeedbackViewModel(feedbackUseCase)

    /**
     * Try to send feedback success.
     */
    @Test
    fun tryToSendFeedback_success() = runBlockingUnit {
        coEvery { feedbackUseCase.tryToSendFeedback(any(), any()) } returns true
        feedbackViewModel.isLoading.collectPost {
            feedbackViewModel.tryToSendFeedback(TEST_STRING, FeedbackType.IDEA).joinWithTimeout()
            feedbackViewModel.resetNavigation.getOrAwaitValue(true)
        }.apply {
            assertEquals(2, this.size)
            assertEquals(true, first())
            assertEquals(false, last())
        }
    }

    /**
     * Try to send feedback failed.
     */
    @Test
    fun tryToSendFeedback_failed() = runBlockingUnit {
        coEvery { feedbackUseCase.tryToSendFeedback(any(), any()) } returns false
        feedbackViewModel.isLoading.collectPost {
            feedbackViewModel.tryToSendFeedback(TEST_STRING, FeedbackType.IDEA).joinWithTimeout()
        }.apply {
            assertEquals(2, this.size)
            assertEquals(true, first())
            assertEquals(false, last())
        }
    }
}