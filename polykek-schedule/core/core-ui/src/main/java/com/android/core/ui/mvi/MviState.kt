package com.android.core.ui.mvi

import com.android.core.ui.fragments.SearchToolbarFragment
import com.android.core.ui.viewModels.SearchViewModel

/**
 * Mvi state - marker interface.
 */
interface MviState

/**
 * Search state for [SearchToolbarFragment] and [SearchViewModel].
 */
interface SearchState : MviState {
    val keyWord: String?
}