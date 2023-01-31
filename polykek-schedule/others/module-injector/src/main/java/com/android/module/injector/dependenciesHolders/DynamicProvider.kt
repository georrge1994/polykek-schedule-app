package com.android.module.injector.dependenciesHolders

import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Dynamic provider is a wrapper class which provides dependencies without storing the direct links.
 * The main idea is using the weak references for dependencies.
 *
 * @param T Type of dependencies
 * @property invokeDependencyHolder Component holder
 * @constructor Create [DynamicProvider]
 */
class DynamicProvider<T : IModuleDependencies>(val invokeDependencyHolder: () -> T)