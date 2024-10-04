package com.android.core.ui.newUI.label

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.android.core.ui.theme.AppTypography

/**
 * Bold Label Text.
 */
@Composable
fun BoldLabelText(text: String) {
    Text(
        text = text,
        style = AppTypography.subtitle1
    )
}