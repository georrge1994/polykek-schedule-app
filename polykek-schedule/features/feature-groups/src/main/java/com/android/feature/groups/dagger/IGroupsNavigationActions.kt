package com.android.feature.groups.dagger

import androidx.fragment.app.Fragment

/**
 * Groups navigation actions.
 */
interface IGroupsNavigationActions {
    /**
     * Get main fragment.
     *
     * @return [Fragment]
     */
    fun getMainFragment(): Fragment
}