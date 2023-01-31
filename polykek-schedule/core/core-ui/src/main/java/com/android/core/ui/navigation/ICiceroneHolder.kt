package com.android.core.ui.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

/**
 * Cicerone holder.
 */
interface ICiceroneHolder {
    /**
     * Get main cicerone.
     *
     * @return [Cicerone]
     */
    fun getMainCicerone(): Cicerone<Router>

    /**
     * Get cicerone.
     *
     * @param containerTag Container tag
     * @return [Cicerone]
     */
    fun getCicerone(containerTag: String): Cicerone<Router>
}