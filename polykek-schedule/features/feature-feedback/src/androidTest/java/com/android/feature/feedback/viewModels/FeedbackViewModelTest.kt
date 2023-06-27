package com.android.feature.feedback.viewModels

import com.android.feature.feedback.models.FeedbackType
import com.android.feature.feedback.mvi.FeedbackAction
import com.android.feature.feedback.mvi.FeedbackIntent
import com.android.feature.feedback.mvi.FeedbackState
import com.android.feature.feedback.useCases.FeedbackUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeoutException

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
        val actionJob = feedbackViewModel.action.subscribeAndCompareFirstValue(FeedbackAction.Exit)
        feedbackViewModel.state.collectPost {
            feedbackViewModel.dispatchIntentAsync(FeedbackIntent.Send(TEST_STRING, FeedbackType.IDEA)).joinWithTimeout()
        }.apply {
            assertEquals(3, this.size)
            assertTrue(this[0] is FeedbackState.Default)
            assertTrue(this[1]!!.isLoading)
            assertFalse(this[2]!!.isLoading)
            actionJob.joinWithTimeout()
        }
    }

    /**
     * Try to send feedback failed.
     */
    @Test(expected = TimeoutException::class)
    fun tryToSendFeedback_failed() = runBlockingUnit {
        coEvery { feedbackUseCase.tryToSendFeedback(any(), any()) } returns false
        val actionJob = feedbackViewModel.action.subscribeAndCompareFirstValue(FeedbackAction.Exit)
        feedbackViewModel.state.collectPost {
            feedbackViewModel.dispatchIntentAsync(FeedbackIntent.Send(TEST_STRING, FeedbackType.IDEA)).joinWithTimeout()
        }.apply {
            assertEquals(3, this.size)
            assertTrue(this[0] is FeedbackState.Default)
            assertTrue(this[1]!!.isLoading)
            assertFalse(this[2]!!.isLoading)
            actionJob.joinWithTimeout()
        }
    }
}