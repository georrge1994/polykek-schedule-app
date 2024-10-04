package com.android.feature.welcome.viewModels

import com.android.feature.welcome.R
import com.android.feature.welcome.mvi.WelcomeAction
import com.android.feature.welcome.mvi.WelcomeIntent
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeoutException

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
        assertEquals(R.drawable.welcome_4, welcomeViewModel.getDrawableId(4))
        assertEquals(R.drawable.welcome_4, welcomeViewModel.getDrawableId(5))
    }

    /**
     * Check show role action.
     */
    @Test
    fun checkShowRoleAction() = runBlockingUnit {
        welcomeViewModel.dispatchIntentAsync(WelcomeIntent.ShowRoleScreen).joinWithTimeout()
        welcomeViewModel.action.subscribeAndCompareFirstValue(WelcomeAction.ShowRoleScreen)
    }

    /**
     * Check professor action.
     */
    @Test
    fun checkProfessorAction() = runBlockingUnit {
        welcomeViewModel.dispatchIntentAsync(WelcomeIntent.ShowProfessorScreen).joinWithTimeout()
        welcomeViewModel.action.subscribeAndCompareFirstValue(WelcomeAction.ShowProfessorScreen)
    }

    /**
     * Check schools action.
     */
    @Test
    fun checkSchoolsAction() = runBlockingUnit {
        welcomeViewModel.dispatchIntentAsync(WelcomeIntent.ShowSchoolScreen).joinWithTimeout()
        welcomeViewModel.action.subscribeAndCompareFirstValue(WelcomeAction.ShowProfessorScreen)
    }

    /**
     * New title.
     */
    @Test
    fun newTitle() = runBlockingUnit {
        checkTitle(0, R.string.welcome_screen_message_0)
        checkTitle(1, R.string.welcome_screen_message_1)
        checkTitle(2, R.string.welcome_screen_message_2)
        checkTitle(3, R.string.welcome_screen_message_3)
        checkTitle(4, R.string.welcome_screen_message_4)
        checkTitle(5, R.string.welcome_screen_message_4)
    }

    /**
     * Will emit 2 same actions, but state should change only once.
     */
    @Test(expected = TimeoutException::class)
    fun newTitle_avoidDuplicates() = runBlockingUnit {
        welcomeViewModel.state.collectPost(count = 2) {
            welcomeViewModel.dispatchIntentAsync(WelcomeIntent.ChangeTabPosition(0)).joinWithTimeout()
            welcomeViewModel.dispatchIntentAsync(WelcomeIntent.ChangeTabPosition(0)).joinWithTimeout()
        }
    }

    /**
     * Check title.
     *
     * @param position Position
     * @param expectedTitle Expected title
     */
    private suspend fun checkTitle(position: Int, expectedTitle: Int) {
        welcomeViewModel.dispatchIntentAsync(WelcomeIntent.ChangeTabPosition(position)).joinWithTimeout()
        assertEquals(expectedTitle, welcomeViewModel.state.value.titleResId)
    }
}