package com.android.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val AppColorPalette = lightColors(
    primary = ColorPrimary,
    primaryVariant = ColorPrimaryDark,
    secondary = ColorAccent,
    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = Grey500,
    onBackground = Grey500,
    onSurface = Grey500,
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = AppColorPalette,
        typography = AppTypography,
        shapes = Shapes(
            small = RoundedCornerShape(SmallPadding),
            medium = RoundedCornerShape(MediumPadding),
            large = RoundedCornerShape(LargePadding)
        ),
        content = content
    )
}