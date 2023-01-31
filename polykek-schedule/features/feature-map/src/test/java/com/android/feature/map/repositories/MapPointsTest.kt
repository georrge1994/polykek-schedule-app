package com.android.feature.map.repositories

import com.android.test.support.unitTest.BaseUnitTest
import org.junit.Test

/**
 * Map points test for [MapPoints].
 *
 * @constructor Create empty constructor for map points test
 */
class MapPointsTest : BaseUnitTest() {
    private val mapPoints = MapPoints()

    /**
     * Check some points.
     */
    @Test
    fun checkSomePoints() {
        mapPoints.getPoint("Научно-исследовательский корпус")?.apply {
            assert(latitude == 60.006269)
            assert(longitude == 30.379298)
        } ?: assert(false)
        mapPoints.getPoint("ВИТ")?.apply {
            assert(latitude == 60.009296)
            assert(longitude == 30.387688)
        } ?: assert(false)
        mapPoints.getPoint("ЦНИИ конструкционных материалов «Прометей»")?.apply {
            assert(latitude == 59.948505)
            assert(longitude == 30.381346)
        } ?: assert(false)
        mapPoints.getPoint("Государственный Эрмитаж")?.apply {
            assert(latitude == 59.989436)
            assert(longitude == 30.255169)
        } ?: assert(false)
        mapPoints.getPoint("НИИ ПТ")?.apply {
            assert(latitude == 60.009125)
            assert(longitude == 30.361026)
        } ?: assert(false)
    }
}