package com.android.feature.feedback.useCases

import android.app.Application
import android.os.Build
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.feature.feedback.R
import com.android.feature.feedback.api.FeedbackApiRepository
import com.android.feature.feedback.models.Feedback
import com.android.feature.feedback.models.FeedbackType
import com.android.shared.code.utils.markers.IUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Named

private const val MIN_LENGTH_OF_MESSAGE = 20

/**
 * Provides request to firebase with user feedback.
 *
 * @property application Application object to get context
 * @property feedbackApiRepository Provides communication with firebase cloud
 * @property backgroundMessageBus Common background message bus with snack-bar output
 * @constructor Create [FeedbackUseCase]
 */
internal class FeedbackUseCase @Inject constructor(
    private val application: Application,
    private val feedbackApiRepository: FeedbackApiRepository,
    @Named(BACKGROUND_MESSAGE_BUS) private val backgroundMessageBus: MutableSharedFlow<String>
) : IUseCase {
    /**
     * Try to send feedback.
     *
     * @param message Message
     * @param type Type
     * @return Is successful
     */
    internal suspend fun tryToSendFeedback(message: String, type: FeedbackType): Boolean {
        if (message.length < MIN_LENGTH_OF_MESSAGE) {
            backgroundMessageBus.emit(application.getString(R.string.feedback_fragment_wrong_length_of_message))
            return false
        }
        Feedback(
            message = message,
            type = type,
            date = Date(),
            deviceModel = Build.MODEL,
            token = feedbackApiRepository.getFirebaseToken()
        ).let { feedback ->
            feedbackApiRepository.pushFeedback(feedback).let { isSuccessful ->
                val messageResId = if (isSuccessful)
                    R.string.feedback_fragment_message_sent_successfully
                else
                    R.string.feedback_fragment_message_was_not_sent
                backgroundMessageBus.emit(application.getString(messageResId))
                return isSuccessful
            }
        }
    }
}