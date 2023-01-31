package com.android.module.injector.dependenciesHolders

import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Base dependency holder interface.
 *
 * @param D Output [IModuleDependencies] type
 */
interface IBaseDependencyHolder<D : IModuleDependencies> {
    val dependencies: D
}
