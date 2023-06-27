package com.android.feature.main.screen.menu.viewModels

import android.view.View
import com.android.common.models.savedItems.SavedItem
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.feature.main.screen.R
import com.android.feature.main.screen.menu.mvi.MenuAction
import com.android.feature.main.screen.menu.mvi.MenuIntent
import com.android.shared.code.utils.general.DateFactory
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.testFixtures.waitActiveSubscription
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

/**
 * Bottom animation view model test for [BottomAnimationViewModel].
 *
 * @constructor Create empty constructor for bottom animation view model test
 */
class BottomAnimationViewModelTest : BaseViewModelUnitTest() {
    private val savedItemMock = SavedItem(id = 1, name = "1083/1")
    private val savedItemFlow = MutableSharedFlow<SavedItem?>()
    private val dateFactory: DateFactory = mockk()
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk {
        coEvery { getSelectedItemFlow() } returns savedItemFlow
    }
    private lateinit var bottomAnimationViewModel: BottomAnimationViewModel

    override fun beforeTest() {
        super.beforeTest()
        val usualDay = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DATE, 1)
        }.time
        coEvery { dateFactory.getToday() } returns usualDay
        bottomAnimationViewModel = BottomAnimationViewModel(
            application = application,
            dateFactory = dateFactory,
            savedItemsRoomRepository = savedItemsRoomRepository
        )
    }

    /**
     * Subscription test.
     */
    @Test
    fun subscriptionTest() = runBlockingUnit {
        bottomAnimationViewModel.asyncSubscribe().joinWithTimeout()
        savedItemFlow.waitActiveSubscription().emitAndWait(savedItemMock).joinWithTimeout()
        assertEquals(savedItemMock.name, bottomAnimationViewModel.state.getOrAwaitValue().title)
        bottomAnimationViewModel.unSubscribe()
    }

    /**
     * Update bottom animation - STATE_COLLAPSED.
     */
    @Test
    fun updateBottomAnimation() = runBlockingUnit {
        val actionJob = bottomAnimationViewModel.action.subscribeAndCompareFirstValue(
            MenuAction.ChangeMenuState(STATE_COLLAPSED)
        )
        bottomAnimationViewModel.dispatchIntentAsync(
            MenuIntent.ChangeStateOfBottomBar(STATE_COLLAPSED)
        ).joinWithTimeout()
        actionJob.joinWithTimeout()
    }

    /**
     * Update ui by offset start.
     */
    @Test
    fun updateUiByOffset_start() = runBlockingUnit {
        val slideOffset = 0.32f
        bottomAnimationViewModel.dispatchIntentAsync(MenuIntent.UpdateUiByOffset(offset = slideOffset))
            .joinWithTimeout()
        bottomAnimationViewModel.state.getOrAwaitValue().apply {
            assertEquals(slideOffset, colorMixCoefficient)
            assertEquals(1f - 3f * slideOffset, moreBtnAlpha)
            assertEquals(R.drawable.ic_more_vertical_grey_24dp, moreBtnChevron)
            assertEquals(slideOffset, bottomNavigationViewShift)
            assertEquals(View.VISIBLE, bottomNavigationViewVisibility)
            assertEquals(1f - 3f * slideOffset, bottomNavigationViewAlpha)
        }
    }

    /**
     * Update UI by offset (middle of swiping).
     */
    @Test
    fun updateUiByOffset_middle() = runBlockingUnit {
        bottomAnimationViewModel.dispatchIntentAsync(MenuIntent.UpdateUiByOffset(offset = 0.5f)).joinWithTimeout()
        bottomAnimationViewModel.state.getOrAwaitValue().apply {
            assertEquals(0f, moreBtnAlpha)
            assertEquals(0.5f, colorMixCoefficient)
            assertEquals(View.INVISIBLE, bottomNavigationViewVisibility)
            assertEquals(0f, bottomNavigationViewAlpha)
        }
    }

    /**
     * Update UI by offset (end of swiping).
     */
    @Test
    fun updateUiByOffset_end() = runBlockingUnit {
        bottomAnimationViewModel.dispatchIntentAsync(MenuIntent.UpdateUiByOffset(offset = 0.67f)).joinWithTimeout()
        bottomAnimationViewModel.state.getOrAwaitValue().apply {
            assertEquals(0.029999971f, moreBtnAlpha)
            assertEquals(0.67f, colorMixCoefficient)
            assertEquals(R.drawable.ic_keyboard_arrow_down_white_24dp, moreBtnChevron)
        }
    }

    /**
     * Get george ribbon chevron for victory day.
     */
    @Test
    fun updateUiByOffset_georgeRibbon() = runBlockingUnit {
        val victoryDay = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.MAY)
            set(Calendar.DATE, 9)
        }.time
        coEvery { dateFactory.getToday() } returns victoryDay
        // Re-init viewModel for specific test.
        bottomAnimationViewModel = BottomAnimationViewModel(
            application = application,
            dateFactory = dateFactory,
            savedItemsRoomRepository = savedItemsRoomRepository
        )

        val slideOffset = 0.32f
        bottomAnimationViewModel.dispatchIntentAsync(MenuIntent.UpdateUiByOffset(offset = slideOffset))
            .joinWithTimeout()
        assertEquals(R.drawable.ribbon_of_saint_george, bottomAnimationViewModel.state.getOrAwaitValue().moreBtnChevron)
    }
}