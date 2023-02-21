package com.android.feature.map.useCases

import com.android.feature.map.models.YandexMapItem
import com.android.shared.code.utils.markers.IUseCase
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import java.lang.Double.max
import java.lang.Double.min
import javax.inject.Inject

/**
 * This class calculates [BoundingBox] for Yandex-map-kit.
 *
 * @constructor Create empty constructor for zoom calculator
 */
internal class ZoomCalculationUseCase @Inject constructor() : IUseCase {
    private val mainBuildPoint = Point(60.007461, 30.373100)

    /**
     * Get bounding box.
     *
     * @param yandexMapItems Map items
     * @return [BoundingBox]
     */
    internal fun getBoundingBox(yandexMapItems: List<YandexMapItem>): BoundingBox {
        var top: Double
        var bottom: Double
        var left: Double
        var right: Double
        // Init the default box.
        if (yandexMapItems.isEmpty()) {
            top = mainBuildPoint.latitude
            bottom = mainBuildPoint.latitude
            left = mainBuildPoint.longitude
            right = mainBuildPoint.longitude
        } else {
            with(yandexMapItems[0].point) {
                top = latitude
                bottom = latitude
                left = longitude
                right = longitude
            }
        }
        // Expand box to all buildings.
        for (index in 1 until yandexMapItems.size) {
            with(yandexMapItems[index].point) {
                top = max(latitude, top)
                bottom = min(latitude, bottom)
                left = max(longitude, left)
                right = min(longitude, right)
            }
        }
        // We need to add some margin ~20%.
        return BoundingBox(
            Point(
                bottom + 0.2f * check(bottom, top),
                left - 0.2f * check(right, left)
            ),
            Point(
                top - 0.35f * check(bottom, top),
                right + 0.2f * check(right, left)
            )
        )
    }

    /**
     * Check.
     *
     * @param firstBorder First border
     * @param secondBorder Second border
     * @return Padding for alone or calculated padding
     */
    private fun check(firstBorder: Double, secondBorder: Double) = if (firstBorder == secondBorder) {
        0.01
    } else {
        firstBorder - secondBorder
    }
}