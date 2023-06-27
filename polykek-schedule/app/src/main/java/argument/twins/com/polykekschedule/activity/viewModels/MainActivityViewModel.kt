package argument.twins.com.polykekschedule.activity.viewModels

import argument.twins.com.polykekschedule.activity.mvi.ActivityAction
import argument.twins.com.polykekschedule.activity.mvi.ActivityIntent
import argument.twins.com.polykekschedule.activity.mvi.ActivityState
import argument.twins.com.polykekschedule.room.savedItems.SavedItemsRoomRepository
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

/**
 * Main activity view model.
 *
 * @property backgroundMessageBus Common background message bus with snack-bar output
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [MainActivityViewModel]
 */
class MainActivityViewModel @Inject constructor(
    @Named(BACKGROUND_MESSAGE_BUS) private val backgroundMessageBus: MutableSharedFlow<String>,
    private val savedItemsRoomRepository: SavedItemsRoomRepository
) : BaseSubscriptionViewModel<ActivityIntent, ActivityState, ActivityAction>(ActivityState.Default) {
    override suspend fun subscribe() {
        super.subscribe()
        // Subscribe to background message bus.
        backgroundMessageBus.onEach {
            ActivityAction.ShowMessage(it).emitAction()
            // In some cases http request can return exception before subscription initialization. For that case we need
            // replay = 1, but from another side - all messages have to be shown only once. So, reset cache after using.
            backgroundMessageBus.resetReplayCache()
        }.cancelableLaunchInBackground()
    }

    override suspend fun dispatchIntent(intent: ActivityIntent) {
        when (intent) {
            ActivityIntent.ReInitScreen ->
                ActivityAction.InitScreen(savedItemsRoomRepository.isItemSelected).emitAction()
            is ActivityIntent.ShowMessage -> ActivityAction.ShowMessage(intent.message).emitAction()
        }
    }
}