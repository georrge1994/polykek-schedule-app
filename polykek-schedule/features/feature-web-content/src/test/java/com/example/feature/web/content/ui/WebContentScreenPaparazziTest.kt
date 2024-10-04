package com.example.feature.web.content.ui

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.core.ui.theme.AppTheme
import com.android.test.support.unitTest.base.BaseUnitTestForSubscriptions
import com.example.feature.web.content.mvi.WebContentState
import org.junit.Rule
import org.junit.Test

/**
 * Test for the [WebContent].
 */
class WebContentScreenTest : BaseUnitTestForSubscriptions() {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        maxPercentDifference = 0.001
    )

    @Test
    fun newsContentScreenshotTest_twoItems() {
        paparazzi.snapshot {
            AppTheme {
                WebContent(
                    state = WebContentState(
                        url = "https://www.google.com",
                        title = "Breaking News",
                        scrollToElementClass = null
                    ),
                    onBack = {}
                )
            }
        }
    }
}