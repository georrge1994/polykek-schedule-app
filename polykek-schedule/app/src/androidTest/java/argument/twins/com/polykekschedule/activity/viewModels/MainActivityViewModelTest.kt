package argument.twins.com.polykekschedule.activity.viewModels

import argument.twins.com.polykekschedule.activity.mvi.ActivityAction
import argument.twins.com.polykekschedule.activity.mvi.ActivityIntent
import argument.twins.com.polykekschedule.room.savedItems.SavedItemsRoomRepository
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.testFixtures.waitActiveSubscription
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Test

/**
 * Main activity view model test for [MainActivityViewModel].
 *
 * @constructor Create empty constructor for main activity view model test
 */
class MainActivityViewModelTest : BaseViewModelUnitTest() {
    private val backgroundMessageBus = MutableSharedFlow<String>()
    private val savedItemsRoomRepository: SavedItemsRoomRepository = mockk {
        coEvery { isItemSelected } returns true
    }
    private val mainActivityViewModel = MainActivityViewModel(backgroundMessageBus, savedItemsRoomRepository)

    /**
     * Complex text.
     */
    @Test
    fun complexText() = runBlockingUnit {
        mainActivityViewModel.asyncSubscribe().joinWithTimeout()
        val actionJob = mainActivityViewModel.action.subscribeAndCompareFirstValue(
            ActivityAction.ShowMessage(TEST_STRING)
        )
        backgroundMessageBus.waitActiveSubscription().emitAndWait(TEST_STRING).joinWithTimeout()
        actionJob.joinWithTimeout()
        mainActivityViewModel.unSubscribe()
    }

    /**
     * Init screen test.
     */
    @Test
    fun initScreenTest() = runBlockingUnit {
        val actionJob = mainActivityViewModel.action.subscribeAndCompareFirstValue(
            ActivityAction.InitScreen(true)
        )
        mainActivityViewModel.dispatchIntentAsync(ActivityIntent.ReInitScreen).joinWithTimeout()
        actionJob.joinWithTimeout()
    }

    /**
     * Push message.
     */
    @Test
    fun pushMessage() = runBlockingUnit {
        val actionJob = mainActivityViewModel.action.subscribeAndCompareFirstValue(
            ActivityAction.ShowMessage("abc")
        )
        mainActivityViewModel.dispatchIntentAsync(ActivityIntent.ShowMessage("abc")).joinWithTimeout()
        actionJob.joinWithTimeout()
    }
}