package argument.twins.com.polykekschedule.activity.mvi

import com.android.core.ui.mvi.MviAction

/**
 * Activity action.
 *
 * @constructor Create empty constructor for activity action
 */
sealed class ActivityAction : MviAction {
    /**
     * Init screen.
     *
     * @property isSelectedItem
     * @constructor Create [InitScreen]
     */
    data class InitScreen(val isSelectedItem: Boolean) : ActivityAction()

    /**
     * Show message.
     *
     * @property message Message for snackbar or toast bar
     * @constructor Create [ShowMessage]
     */
    data class ShowMessage(val message: String) : ActivityAction()
}