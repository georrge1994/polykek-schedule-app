package com.android.core.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    defaultFontFamily = FontFamily.SansSerif,
    h6 = TextStyle( // Used for titles
        fontSize = 20.sp,
        color = TextColor,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily.SansSerif
    ),
    body1 = TextStyle(
        fontSize = 15.sp,
        color = TextColor,
        fontFamily = FontFamily.SansSerif
    ),
    body2 = TextStyle( // LabelStyle
        fontSize = 15.sp,
        color = LabelColor,
        fontFamily = FontFamily.SansSerif
    ),
    caption = TextStyle( // Small.Label.Style
        fontSize = 13.sp, // Assuming `_13ssp` is 13sp, adjust accordingly
        color = LabelColor,
        fontFamily = FontFamily.SansSerif
    ),
    subtitle1 = TextStyle( // Bold.Label.Style
        fontSize = 15.sp,
        color = TextColor,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif
    ),
    // Add more styles as needed
)