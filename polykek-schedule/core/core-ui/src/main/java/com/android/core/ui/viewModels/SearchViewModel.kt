package com.android.core.ui.viewModels

import com.android.core.ui.mvi.MviAction
import com.android.core.ui.mvi.SearchIntent
import com.android.core.ui.mvi.SearchState

/**
 * Search view model for search toolbar fragment.
 *
 * @constructor Create empty constructor for search view model
 */
abstract class SearchViewModel<I : SearchIntent, S : SearchState, A: MviAction>(
    defaultState: S
) : BaseSubscriptionViewModel<I, S, A>(defaultState) {
    protected val keyWordFromLastState: String?
        get() = currentState.keyWord
}