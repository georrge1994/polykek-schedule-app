package com.android.feature.main.screen.menu.viewModels

import android.app.Application
import android.view.View
import androidx.annotation.DrawableRes
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.feature.main.screen.R
import com.android.feature.main.screen.menu.mvi.MenuAction
import com.android.feature.main.screen.menu.mvi.MenuIntent
import com.android.feature.main.screen.menu.mvi.MenuState
import com.android.shared.code.utils.general.DateFactory
import com.android.shared.code.utils.general.toCalendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.abs

private val VICTORY_PERIOD = 6..12

/**
 * Bottom animation view model.
 *
 * @property application Application object to get context
 * @property dateFactory Provides today day
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [BottomAnimationViewModel]
 */
internal class BottomAnimationViewModel @Inject constructor(
    private val application: Application,
    private val dateFactory: DateFactory,
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : BaseSubscriptionViewModel<MenuIntent, MenuState, MenuAction>(MenuState.Default) {
    @DrawableRes
    private val moreBtnClosedStateChevron: Int = getChevron()

    override suspend fun subscribe() {
        super.subscribe()
        // Subscribe to selected items.
        savedItemsRoomRepository.getSelectedItemFlow().onEach { savedItem ->
            val newTitle = savedItem?.name ?: application.getString(R.string.schedule_choose_group)
            currentState.copyState(title = newTitle).emitState()
        }.cancelableLaunchInBackground()
    }

    override suspend fun dispatchIntent(intent: MenuIntent) {
        when (intent) {
            is MenuIntent.ChangeStateOfBottomBar -> updateBottomAnimation(intent.newState)
            is MenuIntent.UpdateUiByOffset -> updateUiByOffset(intent.offset)
        }
    }

    /**
     * Update bottom animation.
     *
     * @param newState Bar state
     */
    private suspend fun updateBottomAnimation(newState: Int) = withContext(Dispatchers.Default) {
        MenuAction.ChangeMenuState(newState).emitAction()
    }

    /**
     * Update UI by offset.
     *
     * @param slideOffset Slide offset
     */
    private suspend fun updateUiByOffset(slideOffset: Float) = withContext(Dispatchers.Default) {
        when {
            slideOffset < 0.33f -> currentState.copyState(
                colorMixCoefficient = abs(slideOffset),
                moreBtnAlpha = 1f - 3f * slideOffset,
                moreBtnChevron = moreBtnClosedStateChevron,
                bottomNavigationViewShift = slideOffset,
                bottomNavigationViewVisibility = View.VISIBLE,
                bottomNavigationViewAlpha = 1f - 3f * slideOffset
            )
            slideOffset > 0.66f -> currentState.copyState(
                colorMixCoefficient = abs(slideOffset),
                moreBtnAlpha = 3f * (slideOffset - 0.66f),
                moreBtnChevron = R.drawable.ic_keyboard_arrow_down_white_24dp,
            )
            else -> currentState.copyState(
                moreBtnAlpha = 0f,
                colorMixCoefficient = abs(slideOffset),
                bottomNavigationViewVisibility = View.INVISIBLE,
                bottomNavigationViewAlpha = 0f
            )
        }.emitState()
    }

    /**
     * Get chevron.
     *
     * @return Drawable
     */
    private fun getChevron(): Int = with(dateFactory.getToday().toCalendar()) {
        if (get(Calendar.MONTH) == Calendar.MAY && get(Calendar.DATE) in VICTORY_PERIOD)
            R.drawable.ribbon_of_saint_george
        else
            R.drawable.ic_more_vertical_grey_24dp
    }
}