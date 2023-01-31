package com.android.core.ui.dagger

import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Ui module dependencies marker interface.
 */
interface ICoreUiModuleDependencies : IModuleDependencies {
    /**
     * Necessary dependencies for core base ui
     */
    val coreBaseUiDependencies: ICoreUiDependencies
}