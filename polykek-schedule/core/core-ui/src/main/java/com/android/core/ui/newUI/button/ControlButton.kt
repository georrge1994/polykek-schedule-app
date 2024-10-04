package com.android.core.ui.newUI.button

import androidx.compose.runtime.Composable

/**
 * ControlButton is a button that is used to control the UI.
 */
@Composable
fun ControlButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
) {
    BaseMaterialButton(
        onClick = onClick,
        text = text,
        enabled = enabled,
    )
}