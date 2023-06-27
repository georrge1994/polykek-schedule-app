package com.android.feature.main.screen.saved.mvi

import com.android.common.models.savedItems.SavedItem
import com.android.core.ui.mvi.MviIntent

/**
 * Saved item intent.
 *
 * @constructor Create empty constructor for saved item intent
 */
internal sealed class SavedItemIntent : MviIntent {
    /**
     * Select item.
     *
     * @property savedItem Group or professor
     * @constructor Create [SelectItem]
     */
    internal data class SelectItem(val savedItem: SavedItem) : SavedItemIntent()

    /**
     * Remove item.
     *
     * @property savedItem Group or professor
     * @constructor Create [RemoveItem]
     */
    internal data class RemoveItem(val savedItem: SavedItem) : SavedItemIntent()

    /**
     * Open school list screen.
     */
    internal object OpenSchools : SavedItemIntent()

    /**
     * Open professors search screen.
     */
    internal object OpenProfessors : SavedItemIntent()

    /**
     * Open email chooser dialog.
     */
    internal object OpenEmailChooser : SavedItemIntent()
}
