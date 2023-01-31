package argument.twins.com.polykekschedule.activity.viewModels

import com.android.test.support.androidTest.base.BaseAndroidUnitTestForSubscriptions
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Test

/**
 * Notification view model test for [NotificationViewModel].
 *
 * @constructor Create empty constructor for notification view model test
 */
class NotificationViewModelTest : BaseAndroidUnitTestForSubscriptions() {
    private val backgroundMessageBus = MutableSharedFlow<String>(replay = 1)
    private val notificationViewModel = NotificationViewModel(application, backgroundMessageBus)

    /**
     * Push message.
     */
    @Test
    fun pushMessage() = runBlockingUnit {
        notificationViewModel.pushMessage(0).joinWithTimeout()
        backgroundMessageBus.subscribeAndCompareFirstValue(TEST_STRING).joinWithTimeout()
    }
}