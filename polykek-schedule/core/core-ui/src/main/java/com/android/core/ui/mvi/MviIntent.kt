package com.android.core.ui.mvi

import com.android.core.ui.fragments.SearchToolbarFragment
import com.android.core.ui.viewModels.SearchViewModel

/**
 * Mvi intent - marker interface.
 */
interface MviIntent

/**
 * Search intent for [SearchToolbarFragment] and [SearchViewModel].
 */
interface SearchIntent : MviIntent