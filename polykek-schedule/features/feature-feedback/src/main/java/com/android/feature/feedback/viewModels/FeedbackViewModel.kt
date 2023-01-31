package com.android.feature.feedback.viewModels

import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.feature.feedback.models.FeedbackType
import com.android.feature.feedback.useCases.FeedbackUseCase
import com.android.shared.code.utils.liveData.EventLiveData
import javax.inject.Inject

/**
 * This class contains the feedback logic + background async execution.
 *
 * @property feedbackUseCase Feedback use case provides request to firebase
 * @constructor Create [FeedbackViewModel]
 */
internal class FeedbackViewModel @Inject constructor(private val feedbackUseCase: FeedbackUseCase) : BaseSubscriptionViewModel() {
    val resetNavigation = EventLiveData<Boolean>()

    /**
     * Try to send feedback.
     *
     * @param message Message
     * @param type [FeedbackType]
     */
    internal fun tryToSendFeedback(message: String, type: FeedbackType) = executeWithLoadingAnimation {
        val isSuccessful = feedbackUseCase.tryToSendFeedback(message, type)
        if (isSuccessful)
            resetNavigation.postValue(true)
    }
}