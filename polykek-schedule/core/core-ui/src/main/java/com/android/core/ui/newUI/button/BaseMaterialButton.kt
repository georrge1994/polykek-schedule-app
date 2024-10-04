package com.android.core.ui.newUI.button

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.android.core.ui.theme.AppTypography
import com.android.core.ui.theme.TextColor

/**
 * Base Material Button.
 */
@Composable
fun BaseMaterialButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = TextColor
        ),
    ) {
        Text(
            text = text,
            style = AppTypography.body1
        )
    }
}