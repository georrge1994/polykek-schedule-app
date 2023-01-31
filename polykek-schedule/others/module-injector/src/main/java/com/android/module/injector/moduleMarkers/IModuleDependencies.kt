package com.android.module.injector.moduleMarkers

import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder

/**
 * Module dependencies marker interface.
 */
interface IModuleDependencies {
    val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
}