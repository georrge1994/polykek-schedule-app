package com.android.feature.map.useCases

import com.android.common.models.map.Building
import com.android.common.models.schedule.Week
import com.android.feature.map.R
import com.android.feature.map.models.MapInstructions
import com.android.feature.map.models.YandexMapItem
import com.android.feature.map.repositories.MapPoints
import com.android.shared.code.utils.general.getZeroIfNull
import com.android.shared.code.utils.markers.IUseCase
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

/**
 * Latitude and longitude shift to avoid overlapping several marker on one building.
 */
private const val SHIFT = 0.0001

/**
 * Yandex map use case is preparing instructions for Yandex-map.
 *
 * @property zoomCalculationUseCase This class calculates [BoundingBox] for Yandex-map-kit
 * @property mapPoints This class contains points for all Polytech's buildings
 * @constructor Create [MapUseCase]
 */
@Singleton
internal class MapUseCase @Inject constructor(
    private val zoomCalculationUseCase: ZoomCalculationUseCase,
    private val mapPoints: MapPoints
) : IUseCase {
    /**
     * Get map instruction.
     *
     * @param indexOfSelectedDay Index of selected day
     * @param week Week
     * @return [MapInstructions]
     */
    internal fun getMapInstruction(indexOfSelectedDay: Int, week: Week): MapInstructions {
        val yandexMapItems = ArrayList<YandexMapItem>()
        var isExistUnknownPlace = false
        // Some lessons can be held in the same building. We have to shift the same pointers to avoid overlapping.
        val pointCounters = HashMap<String, Int>()
        week.days[indexOfSelectedDay]?.lessons?.forEachIndexed { index, lesson ->
            lesson.buildingNames?.let { name ->
                mapPoints.getPoint(name)?.let { point ->
                    // Get corrected point.
                    val correctedPoint = if (pointCounters.containsKey(name)) {
                        val countOfSamePoints = pointCounters[name].getZeroIfNull()
                        pointCounters[name] = countOfSamePoints + 1
                        point.getShiftedPoint(countOfSamePoints)
                    } else {
                        pointCounters[name] = 1
                        point
                    }
                    // Create map item object and save to list
                    YandexMapItem(
                        point = correctedPoint,
                        imageId = getIcon(index),
                        userData = lesson
                    ).let { item ->
                        yandexMapItems.add(item)
                    }
                } ?: kotlin.run { isExistUnknownPlace = true }
            } ?: kotlin.run { isExistUnknownPlace = true }
        }
        // Get the BoundingBox-object, which based on points.
        val boundingBox = zoomCalculationUseCase.getBoundingBox(yandexMapItems)
        return MapInstructions(
            yandexMapItems = yandexMapItems,
            isExistUnknownPlace = isExistUnknownPlace,
            boundingBox = boundingBox
        )
    }

    /**
     * Get yandex map item.
     *
     * @param building Building
     * @return [YandexMapItem] or null
     */
    internal fun getYandexMapItem(building: Building?): YandexMapItem? {
        building ?: return null
        val point = mapPoints.getPoint(building.name) ?: return null
        return YandexMapItem(
            point,
            R.drawable.ic_location_black_48dp,
            building
        )
    }

    /**
     * Get shifted point. Actually we expect follow shifts:
     * Point(latitude + SHIFT, longitude + SHIFT)
     * Point(latitude + SHIFT, longitude - SHIFT)
     * Point(latitude - SHIFT, longitude + SHIFT)
     * Point(latitude - SHIFT, longitude - SHIFT)
     *
     * @receiver [Point]
     * @param count Count
     * @return Similar, but a bit shifted [Point]
     */
    private fun Point.getShiftedPoint(count: Int): Point =
        Point(latitude + (-1.0).pow(count shr 1) * SHIFT, longitude + (-1.0).pow(count and 1) * SHIFT)

    /**
     * Get icon.
     *
     * @param index Index
     * @return Drawable
     */
    private fun getIcon(index: Int): Int = if (index == 0)
        R.drawable.ic_location_blue_48dp
    else
        R.drawable.ic_location_red_48dp
}