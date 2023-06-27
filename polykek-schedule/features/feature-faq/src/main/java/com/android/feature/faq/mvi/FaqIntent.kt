package com.android.feature.faq.mvi

import com.android.core.ui.mvi.MviIntent

/**
 * Faq intent.
 *
 * @constructor Create empty constructor for faq intent
 */
internal sealed class FaqIntent : MviIntent {
    /**
     * Click by item.
     *
     * @property position Position of item
     * @property isOpened Is description shown
     * @constructor Create [ClickByItem]
     */
    internal data class ClickByItem(val position: Int, val isOpened: Boolean) : FaqIntent()

    /**
     * Open feedback.
     */
    internal object OpenFeedback : FaqIntent()
}