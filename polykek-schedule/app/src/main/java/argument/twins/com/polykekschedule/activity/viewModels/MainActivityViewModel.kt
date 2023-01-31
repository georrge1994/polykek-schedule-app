package argument.twins.com.polykekschedule.activity.viewModels

import argument.twins.com.polykekschedule.room.savedItems.SavedItemsRoomRepository
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.shared.code.utils.liveData.EventLiveData
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
) : BaseSubscriptionViewModel() {
    val messageFromBus = EventLiveData<String>()

    val isItemSelected: Boolean
        get() = savedItemsRoomRepository.isItemSelected

    override suspend fun subscribe() {
        super.subscribe()
        subscribeToBackgroundMessageBus()
    }

    /**
     * Subscribe to background message bus.
     */
    private fun subscribeToBackgroundMessageBus() = backgroundMessageBus.onEach {
        messageFromBus.postValue(it)
    }.cancelableLaunchInBackground()
}