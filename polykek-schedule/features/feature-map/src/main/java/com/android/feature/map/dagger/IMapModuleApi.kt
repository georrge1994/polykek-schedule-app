package com.android.feature.map.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Map api module.
 */
interface IMapModuleApi : IModuleApi {
    /**
     * Get map screen.
     *
     * @return [Fragment]
     */
    fun getMapFragment(): Fragment
}