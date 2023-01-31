package com.android.feature.buildings.dagger

import com.android.core.ui.dagger.ICoreUiModuleDependencies
import retrofit2.Retrofit

/**
 * Buildings module dependencies.
 */
interface IBuildingsModuleDependencies : ICoreUiModuleDependencies {
    val retrofit: Retrofit
}