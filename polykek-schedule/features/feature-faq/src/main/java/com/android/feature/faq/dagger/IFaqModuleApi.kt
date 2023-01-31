package com.android.feature.faq.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Faq api module.
 */
interface IFaqModuleApi : IModuleApi {
    /**
     * Get faq screen.
     *
     * @return [Fragment]
     */
    fun getFaqFragment(): Fragment
}