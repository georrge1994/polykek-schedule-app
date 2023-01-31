package com.android.module.injector.dependenciesHolders

import com.android.module.injector.moduleMarkers.IModuleApi
import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Dependency holder.
 *
 * @param D Output [IModuleDependencies]
 * @property apis List of module api
 * @constructor Create [DependencyHolder]
 */
abstract class DependencyHolder<D : IModuleDependencies>(
    private val apis: List<IModuleApi>
) : IBaseDependencyHolder<D> {
    /**
     * This function is promising to use this [IBaseDependencyHolder] for creation output [IModuleDependencies]. Also it
     * promise to use List<IModuleApi>. Every each item in list [IModuleApi] is a link to some component(important). In result child
     * components will live until parent [IModuleApi] will not die.
     */
    abstract val block: (IBaseDependencyHolder<D>, List<IModuleApi>) -> D

    override val dependencies: D
        get() = block(this, apis)
}