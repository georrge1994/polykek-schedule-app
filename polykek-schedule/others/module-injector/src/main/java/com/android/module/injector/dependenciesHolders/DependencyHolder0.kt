package com.android.module.injector.dependenciesHolders

import com.android.module.injector.moduleMarkers.IModuleApi
import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Dependency holder with zero [IModuleApi] input parameters.
 *
 * @param D Output [IModuleDependencies]
 * @constructor Create empty constructor for dependency holder 0
 */
abstract class DependencyHolder0<D : IModuleDependencies> : IBaseDependencyHolder<D> {
    /**
     * This function is promising to use this [IBaseDependencyHolder] for creation output [IModuleDependencies].
     */
    abstract val block: (IBaseDependencyHolder<D>) -> D

    override val dependencies: D
        get() = block(this)
}