package argument.twins.com.polykekschedule.activity.useCases

import androidx.activity.ComponentActivity
import argument.twins.com.polykekschedule.R
import by.kirich1409.viewbindingdelegate.internal.findRootView
import com.android.shared.code.utils.general.getZeroIfNull
import com.android.shared.code.utils.markers.IUseCase
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import javax.inject.Inject

private const val DAYS_FOR_FLEXIBLE_UPDATE = 7

/**
 * App update ui use case.
 *
 * @property snackBarUseCase Snackbar use case
 * @constructor Create [AppUpdateUiUseCase]
 */
class AppUpdateUiUseCase @Inject constructor(private val snackBarUseCase: SnackbarUseCase) : IUseCase {
    /**
     * Check app updates.
     *
     * @param activity Activity
     */
    fun checkAppUpdates(activity: ComponentActivity) = AppUpdateManagerFactory.create(activity).let { appUpdateManager ->
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.clientVersionStalenessDays().getZeroIfNull() >= DAYS_FOR_FLEXIBLE_UPDATE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    activity,
                    1
                )
                appUpdateManager.registerListener { state ->
                    when (state.installStatus()) {
                        InstallStatus.DOWNLOADED -> popupSnackBarForCompleteUpdate(activity, appUpdateManager)
                        InstallStatus.INSTALLED -> snackBarUseCase.showMessage(
                            activity,
                            activity.getString(R.string.update_app_installation_was_completed),
                            Snackbar.LENGTH_LONG
                        )
                        else -> {}
                    }
                }
            }
        }
    }

    /**
     * Displays the snackBar notification and call to action.
     *
     * @param activity Activity
     * @param appUpdateManager App update manager
     */
    private fun popupSnackBarForCompleteUpdate(activity: ComponentActivity, appUpdateManager: AppUpdateManager) {
        Snackbar.make(
            findRootView(activity),
            activity.getString(R.string.update_app_downloading_was_completed),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(activity.getString(R.string.update_app_restart)) { appUpdateManager.completeUpdate() }
            show()
        }
    }
}