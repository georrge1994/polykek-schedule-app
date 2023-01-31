package com.android.feature.map.useCases

import com.android.common.models.map.Building
import com.android.common.models.schedule.Day
import com.android.common.models.schedule.Week
import com.android.feature.map.R
import com.android.feature.map.models.YandexMapItem
import com.android.feature.map.repositories.MapPoints
import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.unitTest.BaseUnitTest
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

/**
 * Map use case test for [MapUseCase].
 *
 * @constructor Create empty constructor for map use case test
 */
class MapUseCaseTest : BaseUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()

    // Data.
    private val point = Point(0.0, 0.0)
    private val boundingBox: BoundingBox = mockk()

    // Classes.
    private val zoomCalculationUseCase: ZoomCalculationUseCase = mockk {
        coEvery { getBoundingBox(any()) } returns boundingBox
    }
    private val mapPoints: MapPoints = mockk {
        coEvery { getPoint(any()) } returns point
    }
    private val mapUseCase = MapUseCase(zoomCalculationUseCase, mapPoints)

    /**
     * Get map instruction. Two lessons are in one building - check shift logic. No unexpected places.
     */
    @Test
    fun getMapInstruction() {
        val weekMockk = lessonDataGenerator.getWeekMockk()
        mapUseCase.getMapInstruction(0, weekMockk).let { mapInstructions ->
            assertEquals(2, mapInstructions.yandexMapItems.size)
            assertNotEquals(mapInstructions.yandexMapItems.first().point, mapInstructions.yandexMapItems.last().point)
            assertNotEquals(mapInstructions.yandexMapItems.first().imageId, mapInstructions.yandexMapItems.last().imageId)
            assertFalse(mapInstructions.isExistUnknownPlace)
            assertEquals(boundingBox, mapInstructions.boundingBox)
        }
        coVerify(exactly = 2) { mapPoints.getPoint(any()) }
        coVerify(exactly = 1) { zoomCalculationUseCase.getBoundingBox(any()) }
    }

    /**
     * Get map instruction 2.
     */
    @Test
    fun getMapInstruction2() {
        val weekMockk = Week(
            title = "test week",
            days = mapOf(
                5 to Day(
                    date = "Friday",
                    lessons = listOf(lessonDataGenerator.getLessonMockk())
                )
            )
        )
        coEvery { mapPoints.getPoint(any()) } returns null
        mapUseCase.getMapInstruction(5, weekMockk).let { mapInstructions ->
            assertEquals(0, mapInstructions.yandexMapItems.size)
            assertTrue(mapInstructions.isExistUnknownPlace)
            assertEquals(boundingBox, mapInstructions.boundingBox)
        }
        coVerify(exactly = 1) { mapPoints.getPoint(any()) }
        coVerify(exactly = 1) { zoomCalculationUseCase.getBoundingBox(any()) }
    }

    /**
     * Get yandex map item null building.
     */
    @Test
    fun getYandexMapItem_nullBuilding() {
        assertEquals(null, mapUseCase.getYandexMapItem(null))
    }

    /**
     * Get yandex map item no points for this name.
     */
    @Test
    fun getYandexMapItem_noPointsForThisName() {
        val building = Building(name = "name", nameWithAbbr = "nameWithAbbr", address = "address")
        coEvery { mapPoints.getPoint(any()) } returns null
        assertEquals(null, mapUseCase.getYandexMapItem(building))
    }

    /**
     * Get yandex map item with result.
     */
    @Test
    fun getYandexMapItem_withResult() {
        val building = Building(name = "name", nameWithAbbr = "nameWithAbbr", address = "address")
        assertEquals(
            YandexMapItem(
                point,
                R.drawable.ic_location_black_48dp,
                building
            ),
            mapUseCase.getYandexMapItem(building)
        )
    }
}