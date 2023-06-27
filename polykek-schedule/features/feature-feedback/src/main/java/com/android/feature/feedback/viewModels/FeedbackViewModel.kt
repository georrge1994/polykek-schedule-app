package com.android.feature.feedback.viewModels

import com.android.core.ui.mvi.MviViewModel
import com.android.feature.feedback.models.FeedbackType
import com.android.feature.feedback.mvi.FeedbackAction
import com.android.feature.feedback.mvi.FeedbackIntent
import com.android.feature.feedback.mvi.FeedbackState
import com.android.feature.feedback.useCases.FeedbackUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * This class contains the feedback logic + background async execution.
 *
 * @property feedbackUseCase Feedback use case provides request to firebase
 * @constructor Create [FeedbackViewModel]
 */
internal class FeedbackViewModel @Inject constructor(
    private val feedbackUseCase: FeedbackUseCase
) : MviViewModel<FeedbackIntent, FeedbackState, FeedbackAction>(FeedbackState.Default) {
    override suspend fun dispatchIntent(intent: FeedbackIntent) {
        when (intent) {
            is FeedbackIntent.Send -> tryToSendFeedback(intent.message, intent.type)
        }
    }

    /**
     * Try to send feedback.
     *
     * @param message Message
     * @param type [FeedbackType]
     */
    private suspend fun tryToSendFeedback(message: String, type: FeedbackType) = withContext(Dispatchers.IO) {
        FeedbackState.Update(isLoading = true).emitState()
        val isSuccessful = feedbackUseCase.tryToSendFeedback(message, type)
        FeedbackState.Update(isLoading = false).emitState()
        if (isSuccessful) {
            FeedbackAction.Exit.emitAction()
        }
    }
}