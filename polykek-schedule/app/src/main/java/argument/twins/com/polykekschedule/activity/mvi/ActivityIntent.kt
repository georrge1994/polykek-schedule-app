package argument.twins.com.polykekschedule.activity.mvi

import com.android.core.ui.mvi.MviIntent

/**
 * Activity intent.
 *
 * @constructor Create empty constructor for activity intent
 */
sealed class ActivityIntent : MviIntent {
    /**
     * Re init screen.
     */
    object ReInitScreen : ActivityIntent()

    /**
     * Show message.
     *
     * @property message Message to show
     * @constructor Create [ShowMessage]
     */
    data class ShowMessage(val message: String) : ActivityIntent()
}
