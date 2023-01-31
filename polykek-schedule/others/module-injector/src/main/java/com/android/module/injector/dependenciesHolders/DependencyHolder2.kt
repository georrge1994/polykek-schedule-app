package com.android.module.injector.dependenciesHolders

import com.android.module.injector.moduleMarkers.IModuleApi
import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Dependency holder with two [IModuleApi] input parameters.
 *
 * @param D Output [IModuleDependencies]
 * @constructor Create empty constructor for dependency holder 2
 */
abstract class DependencyHolder2<A1 : IModuleApi, A2 : IModuleApi, D : IModuleDependencies>(
    private val api1: A1,
    private val api2: A2
) : IBaseDependencyHolder<D> {
    /**
     * This function is promising to use this [IBaseDependencyHolder] for creation output [IModuleDependencies]. Also it
     * promise to use [A1]. [A2]. Every each input [IModuleApi] is a link to some component(important). In result child
     * components will live until parent [IModuleApi] will not die.
     */
    abstract val block: (IBaseDependencyHolder<D>, A1, A2) -> D

    override val dependencies: D
        get() = block(this, api1, api2)
}