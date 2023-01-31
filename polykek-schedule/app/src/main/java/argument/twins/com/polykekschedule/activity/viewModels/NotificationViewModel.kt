package argument.twins.com.polykekschedule.activity.viewModels

import android.app.Application
import androidx.annotation.StringRes
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Named

/**
 * Notification view model.
 *
 * @property application Application object to get context
 * @property backgroundMessageBus Common background message bus with snack-bar output
 * @constructor Create [NotificationViewModel]
 */
class NotificationViewModel @Inject constructor(
    private val application: Application,
    @Named(BACKGROUND_MESSAGE_BUS) private val backgroundMessageBus: MutableSharedFlow<String>
): BaseSubscriptionViewModel() {
    /**
     * Push message.
     */
    fun pushMessage(@StringRes stringId: Int) = launchInBackground {
        backgroundMessageBus.emit(application.getString(stringId))
    }
}