package com.android.feature.feedback.useCases

import com.android.feature.feedback.api.FeedbackApiRepository
import com.android.feature.feedback.models.FeedbackType
import com.android.test.support.androidTest.base.BaseAndroidUnitTestForSubscriptions
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Feedback use case test.
 *
 * @constructor Create empty constructor for feedback use case test
 */
class FeedbackUseCaseTest : BaseAndroidUnitTestForSubscriptions() {
    private val feedbackApiRepository: FeedbackApiRepository = mockk {
        coEvery { getFirebaseToken() } returns TEST_STRING
    }
    private val backgroundMessageBus = MutableSharedFlow<String>(replay = 1)
    private val feedbackUseCase = FeedbackUseCase(
        application = application,
        feedbackApiRepository = feedbackApiRepository,
        backgroundMessageBus = backgroundMessageBus
    )

    /**
     * Try to send feedback too short message.
     */
    @Test
    fun tryToSendFeedback_tooShortMessage() = runBlockingUnit {
        assertFalse(feedbackUseCase.tryToSendFeedback("12", FeedbackType.OTHER))
        backgroundMessageBus.subscribeAndCompareFirstValue(TEST_STRING).joinWithTimeout()
        coVerify(exactly = 0) {
            feedbackApiRepository.getFirebaseToken()
            feedbackApiRepository.pushFeedback(any())
        }
    }

    /**
     * Try to send feedback fail.
     */
    @Test
    fun tryToSendFeedback_fail() = runBlockingUnit {
        coEvery { feedbackApiRepository.pushFeedback(any()) } returns false
        assertFalse(
            feedbackUseCase.tryToSendFeedback(
                "the long complex message with length more than 20 symbols",
                FeedbackType.OTHER
            )
        )
        backgroundMessageBus.subscribeAndCompareFirstValue(TEST_STRING).joinWithTimeout()
        coVerify(exactly = 1) {
            feedbackApiRepository.getFirebaseToken()
            feedbackApiRepository.pushFeedback(any())
        }
    }

    /**
     * Try to send feedback success.
     */
    @Test
    fun tryToSendFeedback_success() = runBlockingUnit {
        coEvery { feedbackApiRepository.pushFeedback(any()) } returns true
        assertTrue(
            feedbackUseCase.tryToSendFeedback(
                "the long complex message with length more than 20 symbols",
                FeedbackType.OTHER
            )
        )
        backgroundMessageBus.subscribeAndCompareFirstValue(TEST_STRING).joinWithTimeout()
        coVerify(exactly = 1) {
            feedbackApiRepository.getFirebaseToken()
            feedbackApiRepository.pushFeedback(any())
        }
    }
}