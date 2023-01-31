package com.android.core.ui.dagger

import com.android.core.ui.navigation.ICiceroneHolder
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Core ui module api.
 */
interface ICoreUiModuleApi : IModuleApi {
    /**
     * Necessary dependencies for core base ui
     */
    val coreUiDependencies: ICoreUiDependencies

    /**
     * Cicerone holder (contains router for main screen and every each tab).
     */
    val routerHolder: ICiceroneHolder
}