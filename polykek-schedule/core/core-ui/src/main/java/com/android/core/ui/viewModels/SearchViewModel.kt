package com.android.core.ui.viewModels

/**
 * Search view model for search toolbar fragment.
 *
 * @constructor Create empty constructor for search view model
 */
abstract class SearchViewModel : BaseSubscriptionViewModel() {
    var keyWord: String = ""
        private set

    /**
     * Changed key word async.
     *
     * @param keyWord Key word
     */
    fun updateKeyWordAsync(keyWord: String?) = launchInBackground {
        keyWordWasChanged(keyWord)
    }

    /**
     * Key word was changed.
     *
     * @param keyWord Key word
     */
    protected open suspend fun keyWordWasChanged(keyWord: String?) {
        this.keyWord = keyWord ?: ""
    }
}