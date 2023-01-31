package com.android.feature.map.useCases

import com.android.feature.map.models.DayControls
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

private const val MONDAY = 0
private const val SATURDAY = 5

/**
 * Day controls use case.
 *
 * @constructor Create empty constructor for day controls use case
 */
internal class DayControlsUseCase @Inject constructor() : IUseCase {
    /**
     * Get day controls.
     *
     * @param indexOdDay Index od day
     * @return
     */
    internal fun getDayControls(indexOdDay: Int) = DayControls(
        isPreviousEnabled = indexOdDay > MONDAY,
        isNextEnabled = indexOdDay < SATURDAY,
        dayIndex = max(0, min(indexOdDay, SATURDAY))
    )
}