package com.android.feature.welcome.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Welcome module api.
 */
interface IWelcomeModuleApi : IModuleApi {
    /**
     * Get welcome fragment.
     *
     * @return [Fragment]
     */
    fun getWelcomeFragment(): Fragment
}