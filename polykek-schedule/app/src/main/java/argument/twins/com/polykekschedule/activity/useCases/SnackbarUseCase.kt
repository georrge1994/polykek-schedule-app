package argument.twins.com.polykekschedule.activity.useCases

import androidx.activity.ComponentActivity
import argument.twins.com.polykekschedule.R
import by.kirich1409.viewbindingdelegate.internal.findRootView
import com.android.shared.code.utils.markers.IUseCase
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Snack bar use case to avoid duplicating code.
 *
 * @constructor Create empty constructor for snack bar use case
 */
@Singleton
class SnackbarUseCase @Inject constructor() : IUseCase {
    /**
     * Show message.
     *
     * @param activity Activity
     * @param message Message
     * @param duration Duration
     */
    fun showMessage(
        activity: ComponentActivity,
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT
    ) {
        Snackbar.make(findRootView(activity), message, duration).apply {
            this.view.translationY -= view.resources.getDimensionPixelSize(R.dimen.portrait_bottom_bar_height).toFloat()
            show()
        }
    }
}