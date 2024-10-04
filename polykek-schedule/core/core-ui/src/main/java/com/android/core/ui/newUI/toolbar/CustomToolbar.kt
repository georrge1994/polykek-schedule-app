package com.android.core.ui.newUI.toolbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.core.ui.theme.AppTheme
import com.android.core.ui.theme.AppTypography
import com.android.core.ui.theme.ColorPrimary
import com.android.core.ui.theme.White

/**
 * Custom Toolbar.
 */
@Composable
fun CustomToolbar(
    title: String,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.testTag("TopAppBarTitle"),
                color = White,
                style = AppTypography.h6
            )
        },
        modifier = Modifier.testTag("CustomToolbar"),
        navigationIcon = if (onNavigationClick == null) null else {
            {
                IconButton(
                    onClick = onNavigationClick,
                    modifier = Modifier.testTag("TopAppBarBackButton")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        actions = actions,
        backgroundColor = ColorPrimary,
        contentColor = White,
        elevation = 4.dp,
    )
}

@Preview(showBackground = true)
@Composable
fun CustomToolbarPreview() {
    AppTheme {
        CustomToolbar(
            title = "Preview Title",
            onNavigationClick = { /* Do nothing for preview */ },
            actions = {
                // Add any actions you'd like to preview
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomToolbarPreview2() {
    AppTheme {
        CustomToolbar(
            title = "Preview Title # 2",
            actions = {
                // Add any actions you'd like to preview
            }
        )
    }
}