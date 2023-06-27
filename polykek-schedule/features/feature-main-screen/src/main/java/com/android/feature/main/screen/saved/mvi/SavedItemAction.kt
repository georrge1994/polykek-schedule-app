package com.android.feature.main.screen.saved.mvi

import com.android.core.ui.mvi.MviAction

/**
 * Saved item action.
 *
 * @constructor Create empty constructor for saved item action
 */
internal sealed class SavedItemAction : MviAction {
    /**
     * Open schools.
     */
    internal object OpenSchools : SavedItemAction()

    /**
     * Open professors.
     */
    internal object OpenProfessors : SavedItemAction()

    /**
     * Open email chooser.
     *
     * @property groupName Name of selected group
     * @constructor Create [OpenEmailChooser]
     */
    internal data class OpenEmailChooser(val groupName: String) : SavedItemAction()
}
