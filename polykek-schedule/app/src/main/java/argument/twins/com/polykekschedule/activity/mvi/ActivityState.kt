package argument.twins.com.polykekschedule.activity.mvi

import com.android.core.ui.mvi.MviState

/**
 * Activity state.
 *
 * @constructor Create empty constructor for activity state
 */
sealed class ActivityState : MviState {
    /**
     * Default.
     */
    object Default : ActivityState()
}
