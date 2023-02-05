package com.android.module.injector.dependenciesHolders

import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Dynamic dependencies holder.
 */
interface IDynamicDependenciesHolder <R : IModuleDependencies> {
    var dependenciesProvider: DynamicProvider<R>?
}