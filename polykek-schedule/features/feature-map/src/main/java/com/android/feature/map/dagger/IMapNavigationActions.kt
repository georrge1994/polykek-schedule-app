package com.android.feature.map.dagger

import androidx.fragment.app.Fragment

/**
 * Map navigation actions.
 */
interface IMapNavigationActions {
    /**
     * Get buildings screen.
     *
     * @return [Fragment]
     */
    fun getBuildingsScreen(): Fragment
}