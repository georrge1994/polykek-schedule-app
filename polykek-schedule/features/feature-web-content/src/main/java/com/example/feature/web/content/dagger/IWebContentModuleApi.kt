package com.example.feature.web.content.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Web content api module.
 */
interface IWebContentModuleApi : IModuleApi {
    /**
     * Get web content screen.
     *
     * @param title Title of the web content
     * @param url URL of the web content
     * @param scrollToElementClass Specific class to scroll to
     * @return [Fragment]
     */
    fun getWebContentFragment(title: String, url: String, scrollToElementClass: String? = null): Fragment
}