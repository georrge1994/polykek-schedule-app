package argument.twins.com.polykekschedule.activity.useCases

import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import argument.twins.com.polykekschedule.R
import argument.twins.com.polykekschedule.activity.view.HeartFallView
import com.android.shared.code.utils.general.SharedPreferenceUtils
import com.android.shared.code.utils.markers.IUseCase
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val USER_ALREADY_JUGGED = "USER_ALREADY_JUGGED"
private const val IN_APP_REVIEW_TIMER = "IN_APP_REVIEW_TIMER"
private const val SEVEN_DAYS = 7 * 24 * 60 * 60 * 1000L
private const val TIME_OF_ANIMATION = 4_000L

/**
 * In app review use case.
 *
 * @property sharedPreferenceUtils Shared preference utils provides work wit shared preference memory
 * @property snackBarUseCase Snackbar use case
 * @constructor Create [InAppReviewUiUseCase]
 */
class InAppReviewUiUseCase @Inject constructor(
    private val sharedPreferenceUtils: SharedPreferenceUtils,
    private val snackBarUseCase: SnackbarUseCase
) : IUseCase {
    /**
     * Check and suggest if need.
     *
     * @param activity Activity
     */
    fun checkAndSuggestToRateIfNeed(activity: ComponentActivity) {
        if (sharedPreferenceUtils.contains(USER_ALREADY_JUGGED))
            return
        if (sharedPreferenceUtils.contains(IN_APP_REVIEW_TIMER)) {
            if (System.currentTimeMillis() - sharedPreferenceUtils.getLong(IN_APP_REVIEW_TIMER) > SEVEN_DAYS) {
                ReviewManagerFactory.create(activity).let { manager ->
                    manager.requestReviewFlow().addOnCompleteListener { request ->
                        if (request.isSuccessful) {
                            manager.launchReviewFlow(activity, request.result).addOnSuccessListener {
                                sharedPreferenceUtils.add(USER_ALREADY_JUGGED, true)
                                showAnimation(activity)
                            }
                        }
                    }
                }
            }
        } else {
            sharedPreferenceUtils.add(IN_APP_REVIEW_TIMER, System.currentTimeMillis())
        }
    }

    /**
     * Add view with animation, say thanks to user and remove view after delay.
     *
     * @param activity Activity
     */
    private fun showAnimation(activity: ComponentActivity) = activity.findViewById<ViewGroup>(R.id.activityLayout).let { parentView ->
        HeartFallView(activity).let { heartFall ->
            parentView.addView(heartFall)
            snackBarUseCase.showMessage(activity, activity.getString(R.string.in_app_review_thank_you), Snackbar.LENGTH_LONG)
            activity.lifecycleScope.launch {
                delay(TIME_OF_ANIMATION)
                val auto = Fade().addListener(
                    object : Transition.TransitionListener {
                        override fun onTransitionStart(transition: Transition?) {}
                        override fun onTransitionEnd(transition: Transition?) {
                            parentView.removeView(heartFall)
                        }

                        override fun onTransitionCancel(transition: Transition?) {}
                        override fun onTransitionPause(transition: Transition?) {}
                        override fun onTransitionResume(transition: Transition?) {}
                    }
                )
                TransitionManager.beginDelayedTransition(parentView, auto)
                heartFall.visibility = View.GONE
            }
        }
    }
}