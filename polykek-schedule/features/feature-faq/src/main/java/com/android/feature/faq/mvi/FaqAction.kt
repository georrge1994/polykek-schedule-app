package com.android.feature.faq.mvi

import com.android.core.ui.mvi.MviAction

/**
 * Faq action.
 *
 * @constructor Create empty constructor for faq action
 */
internal sealed class FaqAction : MviAction {
    /**
     * Open feedback.
     */
    internal data object OpenFeedback : FaqAction()
}