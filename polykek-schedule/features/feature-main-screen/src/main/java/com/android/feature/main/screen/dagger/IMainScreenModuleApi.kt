package com.android.feature.main.screen.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Main screen api module.
 */
interface IMainScreenModuleApi : IModuleApi {
    /**
     * Get main fragment.
     *
     * @return [Fragment]
     */
    fun getMainFragment(): Fragment
}