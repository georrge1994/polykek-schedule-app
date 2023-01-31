package com.android.module.injector.dependenciesHolders

import com.android.module.injector.moduleMarkers.IModuleApi
import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Dependency holder with three [IModuleApi] input parameters.
 *
 * @param D Output [IModuleDependencies]
 * @constructor Create empty constructor for dependency holder 3
 */
abstract class DependencyHolder4<A1 : IModuleApi, A2 : IModuleApi, A3 : IModuleApi, A4 : IModuleApi, D : IModuleDependencies>(
    private val api1: A1,
    private val api2: A2,
    private val api3: A3,
    private val api4: A4,
) : IBaseDependencyHolder<D> {
    abstract val block: (IBaseDependencyHolder<D>, A1, A2, A3, A4) -> D

    /**
     * This function is promising to use this [IBaseDependencyHolder] for creation output [IModuleDependencies]. Also it
     * promise to use [A1], [A2], [A3], [A4]. Every each input [IModuleApi] is a link to some component(important). In
     * result child components will live until parent [IModuleApi] will not die.
     */
    override val dependencies: D
        get() = block(this, api1, api2, api3, api4)
}