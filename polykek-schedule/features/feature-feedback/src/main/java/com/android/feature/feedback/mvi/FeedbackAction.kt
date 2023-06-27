package com.android.feature.feedback.mvi

import com.android.core.ui.mvi.MviAction

/**
 * Feedback action.
 *
 * @constructor Create empty constructor for feedback action
 */
internal sealed class FeedbackAction : MviAction {
    /**
     * Reset navigation.
     */
    internal object Exit : FeedbackAction()
}
