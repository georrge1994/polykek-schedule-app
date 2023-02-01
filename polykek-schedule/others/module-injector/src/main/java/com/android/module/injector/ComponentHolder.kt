package com.android.module.injector

import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.moduleMarkers.IModuleApi
import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Component holder stores a link to dependencies and provides method to get API.
 *
 * @param T Output API for some module
 * @param R Input dependencies for same module
 */
abstract class ComponentHolder<T : IModuleApi, R : IModuleDependencies> {
    /**
     * To avoid memory leaking it have exactly to be a provider of dependencies. In result you can get them without
     * storing directly link here and help GC to make his work.
     */
    @Volatile
    protected var dependenciesProvider: DynamicProvider<R>? = null

    /**
     * Init component and his API.
     *
     * @param dependenciesProvider Dependencies provider
     */
    fun initAndGet(dependenciesProvider: DynamicProvider<R>): T {
        this.dependenciesProvider = dependenciesProvider
        return getApi()
    }

    /**
     * Get component API.
     *
     * @return [T]
     */
    protected abstract fun getApi(): T
}