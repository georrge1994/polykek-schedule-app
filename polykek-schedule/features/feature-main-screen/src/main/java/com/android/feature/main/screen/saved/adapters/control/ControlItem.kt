package com.android.feature.main.screen.saved.adapters.control

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Control saved item.
 *
 * @property iconId Icon res for add item
 * @property textId Text for button inside
 * @constructor Create [ControlItem]
 */
internal data class ControlItem(
    @DrawableRes val iconId: Int,
    @StringRes val textId: Int
)