package com.android.feature.welcome.viewModels

import com.android.feature.welcome.R
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Welcome view model test for [WelcomeViewModel].
 *
 * @constructor Create empty constructor for welcome view model test
 */
class WelcomeViewModelTest : BaseViewModelUnitTest() {
    private val welcomeViewModel = WelcomeViewModel()

    /**
     * Get drawable id.
     */
    @Test
    fun getDrawableId() {
        assertEquals(R.drawable.welcome_0, welcomeViewModel.getDrawableId(0))
        assertEquals(R.drawable.welcome_1, welcomeViewModel.getDrawableId(1))
        assertEquals(R.drawable.welcome_2, welcomeViewModel.getDrawableId(2))
        assertEquals(R.drawable.welcome_3, welcomeViewModel.getDrawableId(3))
        assertEquals(R.drawable.welcome_3, welcomeViewModel.getDrawableId(4))
    }

    /**
     * Get label res id.
     */
    @Test
    fun getLabelResId() {
        assertEquals(R.string.welcome_screen_message_0, welcomeViewModel.getLabelResId(0))
        assertEquals(R.string.welcome_screen_message_1, welcomeViewModel.getLabelResId(1))
        assertEquals(R.string.welcome_screen_message_2, welcomeViewModel.getLabelResId(2))
        assertEquals(R.string.welcome_screen_message_3, welcomeViewModel.getLabelResId(3))
        assertEquals(R.string.welcome_screen_message_3, welcomeViewModel.getLabelResId(4))
    }
}