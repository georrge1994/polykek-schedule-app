package com.android.module.injector

import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.moduleMarkers.IModuleApi
import com.android.module.injector.moduleMarkers.IModuleDependencies
import java.lang.ref.SoftReference

/**
 * Component holder provides soft reference to component and logic for creating component.
 *
 * @param T Some module api interface
 * @param R Dependencies for this module
 * @param S Component type. Every each component is inheriting from [IModuleApi]
 * @property componentFactory Factory to provide component from dependencies
 * @constructor Create [WeakComponentHolderDelegate]
 */
class WeakComponentHolderDelegate<T : IModuleApi, R : IModuleDependencies, S : T>(
    private val componentFactory: (R) -> S
) {
    @Volatile
    private var componentWeakRef: SoftReference<S>? = null

    /**
     * Get api for component.
     *
     * @param dependencyProvider Component dependencies provider
     * @return [T]
     */
    @Synchronized
    fun getApi(dependencyProvider: DynamicProvider<R>?): T {
        var component: S? = null
        synchronized(this) {
            dependencyProvider?.let { dependencyProvider ->
                component = componentWeakRef?.get()
                if (component == null) {
                    component = componentFactory(dependencyProvider.invokeDependencyHolder())
                    componentWeakRef = SoftReference(component)
                }
            } ?: throw IllegalStateException("dependencyProvider is not initialized for $componentFactory ")
        }

        return component ?: throw IllegalStateException("Component factory $componentFactory is not initialized")
    }
}