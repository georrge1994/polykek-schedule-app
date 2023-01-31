package com.android.feature.feedback.api

import com.android.feature.feedback.models.Feedback
import com.android.feature.feedback.models.FeedbackType
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.unitTest.base.BaseApiRepositoryTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test
import java.util.*

/**
 * Firebase api repository test.
 *
 * @constructor Create empty constructor for firebase api repository test
 */
class FirebaseApiRepositoryTest : BaseApiRepositoryTest() {
    private val firebaseApiRepository: FirebaseApiRepository = mockk()
    private val feedbackApiRepository = FeedbackApiRepository(firebaseApiRepository)

    /**
     * Push feedback.
     */
    @Test
    fun pushFeedback() = runBlockingUnit {
        val feedback = Feedback(
            message = "my message",
            type = FeedbackType.IDEA,
            date = Date(),
            deviceModel = "Pixel 6 Pro",
            token = "my token"
        )
        coEvery { firebaseApiRepository.updateChildren(any()) } returns true
        assert(feedbackApiRepository.pushFeedback(feedback))
        coVerify(exactly = 1) { firebaseApiRepository.updateChildren(any()) }
    }
}