package com.android.feature.main.screen.menu.viewModels

import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.android.common.models.savedItems.SavedItem
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.feature.main.screen.R
import com.android.shared.code.utils.general.DateFactory
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.getOrAwaitValue
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Bottom animation view model test for [BottomAnimationViewModel].
 *
 * @constructor Create empty constructor for bottom animation view model test
 */
class BottomAnimationViewModelTest : BaseViewModelUnitTest() {
    private val savedItem = MutableLiveData<SavedItem?>()
    private val dateFactory: DateFactory = mockk()
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk {
        coEvery { selectedItemLive2 } returns savedItem
    }
    private lateinit var bottomAnimationViewModel: BottomAnimationViewModel

    override fun beforeTest() {
        super.beforeTest()
        mockkStatic(ContextCompat::class)
        coEvery { ContextCompat.getColor(any(), any()) } returns 0
        bottomAnimationViewModel = BottomAnimationViewModel(application, dateFactory, savedItemsRoomRepository)
    }

    /**
     * Title transformation.
     */
    @Test
    fun titleTransformation() {
        savedItem.postValue(SavedItem(id = 1, name = "1083/1"))
        bottomAnimationViewModel.title.getOrAwaitValue("1083/1")
    }

    /**
     * Update bottom animation - STATE_COLLAPSED.
     */
    @Test
    fun updateBottomAnimation() {
        bottomAnimationViewModel.updateBottomAnimation(state = STATE_COLLAPSED)
        bottomAnimationViewModel.bottomSheetState.getOrAwaitValue(BottomSheetBehavior.STATE_EXPANDED)
        bottomAnimationViewModel.groupToolbarColor.getOrAwaitValue(expectedResult = 0)
        bottomAnimationViewModel.groupNameColor.getOrAwaitValue(expectedResult = 0)
        bottomAnimationViewModel.slideTopPosition.getOrAwaitValue(expectedResult = Pair(0f, 1f))
    }

    /**
     * Update ui by offset - swipe state # N.
     */
    @Test
    fun updateUiByOffset() {
        bottomAnimationViewModel.updateUiByOffset(slideOffset = 0.67f)
        bottomAnimationViewModel.groupToolbarColor.getOrAwaitValue(expectedResult = 0)
        bottomAnimationViewModel.groupNameColor.getOrAwaitValue(expectedResult = 0)
        bottomAnimationViewModel.slideMiddlePosition.getOrAwaitValue(expectedResult = Pair(0.67f, 0.029999971f))
    }

    /**
     * Update ui by offset 2 - swipe state # N + 1.
     */
    @Test
    fun updateUiByOffset2() {
        bottomAnimationViewModel.updateUiByOffset(slideOffset = 0.5f)
        bottomAnimationViewModel.groupToolbarColor.getOrAwaitValue(expectedResult = 0)
        bottomAnimationViewModel.groupNameColor.getOrAwaitValue(expectedResult = 0)
        bottomAnimationViewModel.slideBottomPosition.getOrAwaitValue(expectedResult = Pair(0.5f, 1f))
    }

    /**
     * Get george ribbon chevron for victory day.
     */
    @Test
    fun getChevron_georgeRibbon() {
        val victoryDay = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.MAY)
            set(Calendar.DATE, 9)
        }.time
        coEvery { dateFactory.getToday() } returns victoryDay
        assertEquals(R.drawable.ribbon_of_saint_george, bottomAnimationViewModel.getChevron())
    }

    /**
     * Get dotes chevron for usual day.
     */
    @Test
    fun getChevron_dotes() {
        val usualDay = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DATE, 1)
        }.time
        coEvery { dateFactory.getToday() } returns usualDay
        assertEquals(R.drawable.ic_more_vertical_grey_24dp, bottomAnimationViewModel.getChevron())
    }

    override fun afterTest() {
        super.afterTest()
        unmockkAll()
    }
}