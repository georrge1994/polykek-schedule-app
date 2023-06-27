package com.android.feature.faq.viewModels

import com.android.core.ui.mvi.MviViewModel
import com.android.feature.faq.models.FAQ
import com.android.feature.faq.mvi.FaqAction
import com.android.feature.faq.mvi.FaqIntent
import com.android.feature.faq.mvi.FaqState
import javax.inject.Inject

/**
 * Frequently Asked Question view model.
 *
 * @constructor Create empty constructor for faq view model
 */
internal class FaqViewModel @Inject constructor() : MviViewModel<FaqIntent, FaqState, FaqAction>(FaqState.Default) {
    override suspend fun dispatchIntent(intent: FaqIntent) {
        when (intent) {
            is FaqIntent.ClickByItem -> getNewItems(intent.position, intent.isOpened).emitState()
            is FaqIntent.OpenFeedback -> FaqAction.OpenFeedback.emitAction()
        }
    }

    /**
     * Get new items.
     *
     * @param position Position
     * @param isOpened Is [position] selected/opened
     * @return List of [FAQ]
     */
    private fun getNewItems(position: Int, isOpened: Boolean) = FaqState.Default.items.mapIndexed { index, faq ->
        faq.copy(isOpened = index == position && isOpened)
    }.let { newItems ->
        FaqState.Update(newItems)
    }
}