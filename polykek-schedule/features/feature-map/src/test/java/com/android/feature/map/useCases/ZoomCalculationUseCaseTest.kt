package com.android.feature.map.useCases

import com.android.feature.map.models.YandexMapItem
import com.android.test.support.androidTest.DELTA
import com.android.test.support.unitTest.BaseUnitTest
import com.yandex.mapkit.geometry.Point
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Zoom calculation use case test for [ZoomCalculationUseCase].
 *
 * @constructor Create empty constructor for zoom calculation use case test
 */
class ZoomCalculationUseCaseTest : BaseUnitTest() {
    private val zoomCalculationUseCase = ZoomCalculationUseCase()

    /**
     * Get bounding box.
     */
    @Test
    fun getBoundingBox() {
        val yandexMapItems = listOf(
            YandexMapItem(point = Point(10.0, 10.0), imageId = 0, userData = null),
            YandexMapItem(point = Point(10.0, 20.0), imageId = 0, userData = null),
            YandexMapItem(point = Point(20.0, 10.0), imageId = 0, userData = null),
            YandexMapItem(point = Point(20.0, 20.0), imageId = 0, userData = null)
        )
        zoomCalculationUseCase.getBoundingBox(yandexMapItems).apply {
            assertEquals(southWest.latitude, 7.999999970197678, DELTA)
            assertEquals(southWest.longitude, 22.000000029802322, DELTA)
            assertEquals(northEast.latitude, 23.499999940395355, DELTA)
            assertEquals(northEast.longitude, 8.0, DELTA)
        }
    }
}