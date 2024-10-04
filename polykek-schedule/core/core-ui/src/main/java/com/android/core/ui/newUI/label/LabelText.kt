package com.android.core.ui.newUI.label

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.android.core.ui.theme.AppTypography

/**
 * Label Text.
 */
@Composable
fun LabelText(text: String) {
    Text(
        text = text,
        style = AppTypography.body2
    )
}