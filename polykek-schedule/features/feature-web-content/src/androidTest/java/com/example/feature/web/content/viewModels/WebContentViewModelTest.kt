package com.example.feature.web.content.viewModels

import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.example.feature.web.content.mvi.WebContentIntent
import com.example.feature.web.content.mvi.WebContentState
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Web content view model test for [WebContentViewModel].
 */
class WebContentViewModelTest : BaseViewModelUnitTest() {
    private val webContentViewModel = WebContentViewModel()

    /**
     * Dispatch intent async load web content.
     */
    @Test
    fun dispatchIntentAsync_loadWebContent() = runBlockingUnit {
        val intent = WebContentIntent.LoadWebContent(
            title = "title",
            url = "url",
            specificClass = "specific"
        )

        webContentViewModel.state.collectPost {
            webContentViewModel.dispatchIntentAsync(intent).joinWithTimeout()
        }.apply {
            assertEquals(2, this.size)
            assertEquals(WebContentState.default, this[0])
            assertEquals(intent.title, this[1]?.title)
            assertEquals(intent.url, this[1]?.url)
            assertEquals(intent.specificClass, this[1]?.scrollToElementClass)
        }
    }
}