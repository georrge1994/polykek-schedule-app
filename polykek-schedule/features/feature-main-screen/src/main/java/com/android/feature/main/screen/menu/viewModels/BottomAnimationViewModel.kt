package com.android.feature.main.screen.menu.viewModels

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.viewModels.BaseViewModel
import com.android.feature.main.screen.R
import com.android.shared.code.utils.general.DateFactory
import com.android.shared.code.utils.general.toCalendar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

private val VICTORY_PERIOD = 6..12

/**
 * Bottom animation view model.
 *
 * @property application Application object to get context
 * @property dateFactory Provides today day
 * @constructor Create [BottomAnimationViewModel]
 *
 * @param savedItemsRoomRepository Stores selected groups and professors (selected items)
 */
internal class BottomAnimationViewModel @Inject constructor(
    private val application: Application,
    private val dateFactory: DateFactory,
    savedItemsRoomRepository: ISavedItemsRoomRepository
) : BaseViewModel() {
    private val green = ContextCompat.getColor(application, R.color.colorPrimary)
    private val grey500 = ContextCompat.getColor(application, R.color.grey_500)
    private val white = ContextCompat.getColor(application, R.color.white)

    val title = savedItemsRoomRepository.selectedItemLive2.map { it?.name ?: application.getString(R.string.schedule_choose_group) }

    // Offset + alpha.
    val slideTopPosition = MutableLiveData<Pair<Float, Float>>()
    val slideMiddlePosition = MutableLiveData<Pair<Float, Float>>()
    val slideBottomPosition = MutableLiveData<Pair<Float, Float>>()

    // Colors.
    val groupToolbarColor = MutableLiveData<Int>()
    val groupNameColor = MutableLiveData<Int>()

    val bottomSheetState = MutableLiveData<Int>()

    /**
     * Update bottom animation.
     *
     * @param state State
     */
    internal fun updateBottomAnimation(state: Int) =
        updateBottomAnimation(state == BottomSheetBehavior.STATE_COLLAPSED)

    /**
     * Update bottom animation.
     *
     * @param isOpen Is open
     */
    internal fun updateBottomAnimation(isOpen: Boolean) {
        bottomSheetState.postValue(getState(isOpen))
        updateUiByOffset(0f)
    }

    /**
     * Update ui by offset.
     *
     * @param slideOffset Slide offset
     */
    internal fun updateUiByOffset(slideOffset: Float) {
        groupToolbarColor.postValue(ColorUtils.blendARGB(white, green, abs(slideOffset)))
        groupNameColor.postValue(ColorUtils.blendARGB(grey500, white, abs(slideOffset)))
        when {
            slideOffset < 0.33f -> slideTopPosition.postValue(Pair(slideOffset, 1f - 3f * slideOffset))
            slideOffset > 0.66f -> slideMiddlePosition.postValue(Pair(slideOffset, 3f * (slideOffset - 0.66f)))
            else -> slideBottomPosition.postValue(Pair(slideOffset, 1f))
        }
    }

    /**
     * Get chevron.
     *
     * @return Drawable
     */
    internal fun getChevron(): Int = with(dateFactory.getToday().toCalendar()) {
        if (get(Calendar.MONTH) == Calendar.MAY && get(Calendar.DATE) in VICTORY_PERIOD)
            R.drawable.ribbon_of_saint_george
        else
            R.drawable.ic_more_vertical_grey_24dp
    }

    /**
     * Get state.
     *
     * @param isOpen Is open
     * @return [BottomSheetBehavior]
     */
    private fun getState(isOpen: Boolean) = if (isOpen)
        BottomSheetBehavior.STATE_EXPANDED
    else
        BottomSheetBehavior.STATE_COLLAPSED
}