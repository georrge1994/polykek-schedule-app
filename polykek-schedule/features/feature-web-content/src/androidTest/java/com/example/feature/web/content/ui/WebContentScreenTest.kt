package com.example.feature.web.content.ui

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import com.android.test.support.androidTest.BaseUiComposeTest
import com.example.feature.web.content.mvi.WebContentState
import org.junit.Test

/**
 * Test for the Web Content screen.
 */
class WebContentScreenTest : BaseUiComposeTest() {
    /**
     * Check if the elements are displayed on the screen.
     */
    @Test
    fun webContent_checkElementsOnTheScreen() {
        rule.setContent {
            WebContent(
                state = WebContentState(
                    url = "https://www.google.com",
                    title = "Google",
                    scrollToElementClass = null
                ),
                onBack = {}
            )
        }

        rule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Google")
        rule.onNodeWithTag("TopAppBarBackButton").assertExists()
        rule.onNodeWithTag("WebViewPreview").assertExists()
    }
}