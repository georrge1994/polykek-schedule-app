package argument.twins.com.polykekschedule.activity.useCases

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.android.shared.code.utils.markers.IUseCase
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

/**
 * Small features ui use case.
 *
 * @property snackBarUseCase Show snackbar
 * @property inAppReviewUiUseCase In app review logic
 * @property loveDayUiUseCase Heartfall on 14th February (Valentine day) and 25th June (Petr and Fevronia day)
 * @property appUpdateUiUseCase Check updates and suggest it
 * @property checkPostNotificationPermissionUseCase Notification permission helper for FMS
 * @constructor Create [SmallFeaturesUiUseCase]
 */
class SmallFeaturesUiUseCase @Inject constructor(
    private val snackBarUseCase: SnackbarUseCase,
    private val inAppReviewUiUseCase: InAppReviewUiUseCase,
    private val loveDayUiUseCase: LoveDayUiUseCase,
    private val appUpdateUiUseCase: AppUpdateUiUseCase,
    private val checkPostNotificationPermissionUseCase: CheckPostNotificationPermissionUseCase
) : IUseCase {
    /**
     * Check news.
     *
     * @param activity Activity
     */
    fun checkSmallFeatures(activity: AppCompatActivity) {
        try {
            // Hard solution for https://issuetracker.google.com/issues/200437477.
            inAppReviewUiUseCase.checkAndSuggestToRateIfNeed(activity)
            appUpdateUiUseCase.checkAppUpdates(activity)
        } catch (e : Exception) {
            with(FirebaseCrashlytics.getInstance()) {
                setUserId(Build.DEVICE)
                recordException(e)
            }
        }
        loveDayUiUseCase.checkLoveDay(activity)
    }

    /**
     * Ask notification permission for FMS.
     *
     * @param activity Activity
     */
    fun askNotificationPermissionForFMS(activity: AppCompatActivity) =
        checkPostNotificationPermissionUseCase.askNotificationPermission(activity)

    /**
     * Show snackbar message.
     *
     * @param activity Activity
     * @param message Message
     */
    fun showMessage(activity: AppCompatActivity, message: String) =
        snackBarUseCase.showMessage(activity, message, Snackbar.LENGTH_SHORT)
}