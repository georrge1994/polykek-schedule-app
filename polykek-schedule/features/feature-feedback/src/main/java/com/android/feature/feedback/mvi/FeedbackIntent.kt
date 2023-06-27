package com.android.feature.feedback.mvi

import com.android.core.ui.mvi.MviIntent
import com.android.feature.feedback.models.FeedbackType

/**
 * Feedback intent.
 *
 * @constructor Create empty constructor for feedback intent
 */
internal sealed class FeedbackIntent : MviIntent {
    /**
     * Send.
     *
     * @property message Some text message
     * @property type Type of message
     * @constructor Create [Send]
     */
    internal data class Send(val message: String, val type: FeedbackType) : FeedbackIntent()
}