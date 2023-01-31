package com.android.feature.map.repositories

import com.android.test.support.androidTest.BaseAndroidUnitTest
import com.yandex.mapkit.MapKitFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.Test

/**
 * Map kit initializer test for [MapKitInitializer].
 *
 * @constructor Create empty constructor for map kit initializer test
 */
class MapKitInitializerTest : BaseAndroidUnitTest() {
    override fun beforeTest() {
        super.beforeTest()
        mockkStatic(MapKitFactory::class)
        coEvery { MapKitFactory.setApiKey(any()) } returns Unit
        coEvery { MapKitFactory.initialize(any()) } returns Unit
    }

    /**
     * Complex test.
     */
    @Test
    fun complexTest() {
        MapKitInitializer.initialize(targetContext)
        MapKitInitializer.initialize(targetContext)
        coVerify(exactly = 1) {
            MapKitFactory.setApiKey(any())
            MapKitFactory.initialize(targetContext)
        }
    }

    override fun afterTest() {
        super.afterTest()
        unmockkAll()
    }
}