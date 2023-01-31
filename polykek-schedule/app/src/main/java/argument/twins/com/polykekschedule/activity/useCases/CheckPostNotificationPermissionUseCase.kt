package argument.twins.com.polykekschedule.activity.useCases

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import argument.twins.com.polykekschedule.activity.fragments.NotificationDialogFragment
import com.android.shared.code.utils.general.SharedPreferenceUtils
import javax.inject.Inject
import javax.inject.Singleton

private const val NOTIFICATION_PERMISSION_WAS_ASKED = "NOTIFICATION_PERMISSION_WAS_ASKED"

/**
 * Check post notification permission use case.
 *
 * @property snackBarUseCase Snackbar use case
 * @constructor Create [CheckPostNotificationPermissionUseCase]
 */
@Singleton
class CheckPostNotificationPermissionUseCase @Inject constructor(
    private val snackBarUseCase: SnackbarUseCase,
    private val sharedPreferenceUtils: SharedPreferenceUtils
) {
    /**
     * Ask notification permission.
     *
     * @param activity Activity
     */
    fun askNotificationPermission(activity: AppCompatActivity) = with(activity) {
        if (isNecessaryToShowDialog()) {
            sharedPreferenceUtils.add(NOTIFICATION_PERMISSION_WAS_ASKED, true)
            NotificationDialogFragment().show(supportFragmentManager, NotificationDialogFragment::class.java.name)
        }
    }

    /**
     * Request is only necessary for API level >= 33 (TIRAMISU). App asks only once.
     *
     * @receiver [AppCompatActivity]
     * @return Condition result
     */
    private fun AppCompatActivity.isNecessaryToShowDialog() =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED
            && !sharedPreferenceUtils.contains(NOTIFICATION_PERMISSION_WAS_ASKED)
}