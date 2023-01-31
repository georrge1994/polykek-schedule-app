package argument.twins.com.polykekschedule.activity.useCases

import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.unitTest.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test

/**
 * Small features ui use case test.
 *
 * @constructor Create empty constructor for small features ui use case test
 */
class SmallFeaturesUiUseCaseTest : BaseUnitTest() {
    private val snackBarUseCase: SnackbarUseCase = mockk()
    private val inAppReviewUiUseCase: InAppReviewUiUseCase = mockk()
    private val loveDayUiUseCase: LoveDayUiUseCase = mockk()
    private val appUpdateUiUseCase: AppUpdateUiUseCase = mockk()
    private val checkPostNotificationPermissionUseCase: CheckPostNotificationPermissionUseCase = mockk()
    private val smallFeaturesUiUseCase = SmallFeaturesUiUseCase(
        snackBarUseCase = snackBarUseCase,
        inAppReviewUiUseCase = inAppReviewUiUseCase,
        loveDayUiUseCase = loveDayUiUseCase,
        appUpdateUiUseCase = appUpdateUiUseCase,
        checkPostNotificationPermissionUseCase = checkPostNotificationPermissionUseCase
    )

    /**
     * Check small features.
     */
    @Test
    fun checkSmallFeatures() = runBlockingUnit {
        coEvery { inAppReviewUiUseCase.checkAndSuggestToRateIfNeed(any()) } returns Unit
        coEvery { appUpdateUiUseCase.checkAppUpdates(any()) } returns mockk()
        coEvery { loveDayUiUseCase.checkLoveDay(any()) } returns Unit
        smallFeaturesUiUseCase.checkSmallFeatures(mockk())
        coVerify(exactly = 1) {
            inAppReviewUiUseCase.checkAndSuggestToRateIfNeed(any())
            appUpdateUiUseCase.checkAppUpdates(any())
            loveDayUiUseCase.checkLoveDay(any())
        }
    }

    /**
     * Ask notification permission for fms.
     */
    @Test
    fun askNotificationPermissionForFMS() = runBlockingUnit {
        coEvery { checkPostNotificationPermissionUseCase.askNotificationPermission(any()) } returns Unit
        smallFeaturesUiUseCase.askNotificationPermissionForFMS(mockk())
        coVerify(exactly = 1) { checkPostNotificationPermissionUseCase.askNotificationPermission(any()) }
    }

    /**
     * Show message.
     */
    @Test
    fun showMessage() = runBlockingUnit {
        coEvery { snackBarUseCase.showMessage(any(), any(), any()) } returns Unit
        smallFeaturesUiUseCase.showMessage(mockk(), TEST_STRING)
        coVerify(exactly = 1) { snackBarUseCase.showMessage(any(), TEST_STRING, any()) }
    }
}