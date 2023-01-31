package argument.twins.com.polykekschedule.activity.useCases

import android.view.ViewGroup
import androidx.activity.ComponentActivity
import argument.twins.com.polykekschedule.R
import argument.twins.com.polykekschedule.activity.view.HeartFallView
import com.android.shared.code.utils.general.isToday
import com.android.shared.code.utils.markers.IUseCase
import java.util.*
import javax.inject.Inject

private const val VALENTINE_DAY = 14
private const val PETR_AND_FEVRONIA_DAY = 25

/**
 * Love day use case for 14th February and 25 June.
 *
 * @constructor Create empty constructor for love day ui use case
 */
class LoveDayUiUseCase @Inject constructor() : IUseCase {
    /**
     * Check love day.
     *
     * @param activity Activity
     */
    fun checkLoveDay(activity: ComponentActivity) {
        if (isToday(Calendar.FEBRUARY, VALENTINE_DAY) || isToday(Calendar.JUNE, PETR_AND_FEVRONIA_DAY)) {
            activity.findViewById<ViewGroup>(R.id.activityLayout).addView(HeartFallView(activity))
        }
    }
}