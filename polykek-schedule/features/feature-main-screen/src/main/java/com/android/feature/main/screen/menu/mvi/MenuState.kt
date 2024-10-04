package com.android.feature.main.screen.menu.mvi

import android.view.View
import androidx.annotation.DrawableRes
import com.android.core.ui.mvi.MviState
import com.android.feature.main.screen.R

/**
 * Menu state properties.
 */
internal interface MenuStateProperties {
    val title: String
    val moreBtnAlpha: Float
    val moreBtnChevron: Int
    val colorMixCoefficient: Float
    val bottomNavigationViewShift: Float
    val bottomNavigationViewAlpha: Float
    val bottomNavigationViewVisibility: Int
}

/**
 * Menu state.
 *
 * @constructor Create empty constructor for menu state
 */
internal sealed class MenuState : MviState, MenuStateProperties {
    /**
     * Default menu state.
     */
    internal data object Default : MenuState() {
        override val title: String = ""
        override val moreBtnAlpha: Float = 1f

        @DrawableRes
        override val moreBtnChevron: Int = R.drawable.ic_more_vertical_grey_24dp
        override val colorMixCoefficient: Float = 0f
        override val bottomNavigationViewShift: Float = 0f
        override val bottomNavigationViewAlpha: Float = 1f
        override val bottomNavigationViewVisibility: Int = View.VISIBLE
    }

    /**
     * Update.
     *
     * @property title Group or professor name
     * @property moreBtnAlpha Alpha of more button
     * @property moreBtnChevron Icon of more button
     * @property colorMixCoefficient Coefficient for gradient
     * @property bottomNavigationViewShift Shift for moving the bottom navigation view
     * @property bottomNavigationViewAlpha Alpha of bottom navigation view
     * @property bottomNavigationViewVisibility Visibility state of bottom navigation view
     * @constructor Create [Update]
     */
    internal data class Update(
        override val title: String,
        override val moreBtnAlpha: Float,
        override val moreBtnChevron: Int,
        override val colorMixCoefficient: Float,
        override val bottomNavigationViewShift: Float,
        override val bottomNavigationViewAlpha: Float,
        override val bottomNavigationViewVisibility: Int,
    ) : MenuState()

    /**
     * Copy state.
     *
     * @param title Group or professor name
     * @param moreBtnAlpha Alpha of more button
     * @param moreBtnChevron Icon of more button
     * @param colorMixCoefficient Coefficient for gradient
     * @param bottomNavigationViewShift Shift for moving the bottom navigation view
     * @param bottomNavigationViewAlpha Alpha of bottom navigation view
     * @param bottomNavigationViewVisibility Visibility state of bottom navigation view
     * @return [MenuState]
     */
    internal fun copyState(
        title: String = this.title,
        moreBtnAlpha: Float = this.moreBtnAlpha,
        @DrawableRes moreBtnChevron: Int = this.moreBtnChevron,
        colorMixCoefficient: Float = this.colorMixCoefficient,
        bottomNavigationViewShift: Float = this.bottomNavigationViewShift,
        bottomNavigationViewAlpha: Float = this.bottomNavigationViewAlpha,
        bottomNavigationViewVisibility: Int = this.bottomNavigationViewVisibility,
    ): MenuState = Update(
        title = title,
        moreBtnAlpha = moreBtnAlpha,
        moreBtnChevron = moreBtnChevron,
        colorMixCoefficient = colorMixCoefficient,
        bottomNavigationViewShift = bottomNavigationViewShift,
        bottomNavigationViewAlpha = bottomNavigationViewAlpha,
        bottomNavigationViewVisibility = bottomNavigationViewVisibility,
    )
}
