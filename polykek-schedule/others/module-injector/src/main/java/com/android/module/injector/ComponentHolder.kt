package com.android.module.injector

import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IDynamicDependenciesHolder
import com.android.module.injector.moduleMarkers.IModuleApi
import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Component holder stores a link to dependencies and provides method to get API.
 *
 * @param T Output API for some module
 * @param R Input dependencies for same module
 */
abstract class ComponentHolder<T : IModuleApi, R : IModuleDependencies> : IDynamicDependenciesHolder<R> {
    /**
     * To avoid memory leaking it have exactly to be a provider of dependencies. In result you can get them without
     * storing directly link here and help GC to make his work.
     */
    @Volatile
    override var dependenciesProvider: DynamicProvider<R>? = null

    /**
     * Get component API.
     *
     * @return [T]
     */
    abstract fun getApi(): T
}