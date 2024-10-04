package com.android.test.support.androidTest

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule

/**
 * Base class for compose UI tests.
 */
abstract class BaseUiComposeTest {
    @get:Rule
    val rule = createComposeRule()

    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
}