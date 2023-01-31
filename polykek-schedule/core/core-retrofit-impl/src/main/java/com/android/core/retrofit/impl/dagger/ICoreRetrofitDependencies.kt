package com.android.core.retrofit.impl.dagger

import android.app.Application
import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Core retrofit dependencies.
 */
interface ICoreRetrofitDependencies : IModuleDependencies {
    val application: Application
}