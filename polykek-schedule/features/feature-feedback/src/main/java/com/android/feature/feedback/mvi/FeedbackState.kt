package com.android.feature.feedback.mvi

import com.android.core.ui.mvi.MviState

/**
 * Feedback state properties.
 */
internal interface FeedbackStateProperties {
    val isLoading: Boolean
}

/**
 * Feedback state.
 *
 * @constructor Create empty constructor for feedback state
 */
internal sealed class FeedbackState : MviState, FeedbackStateProperties {
    /**
     * Default feedback state.
     */
    internal object Default : FeedbackState() {
        override val isLoading: Boolean = false
    }

    /**
     * Update.
     *
     * @property isLoading Is loading
     * @constructor Create [Update]
     */
    internal data class Update(override val isLoading: Boolean) : FeedbackState()
}
