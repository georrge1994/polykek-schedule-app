package com.android.feature.buildings.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Building module api.
 */
interface IBuildingsModuleApi : IModuleApi {
    /**
     * Get buildings fragment.
     *
     * @return [Fragment]
     */
    fun getBuildingsFragment(): Fragment
}