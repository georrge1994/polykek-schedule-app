package com.android.core.ui.dagger

import com.android.core.ui.navigation.CiceroneHolder
import com.android.core.ui.navigation.ICiceroneHolder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Contains the implementations for general providers for feature modules and main app module.
 *
 * @constructor Create empty constructor for core ui general module
 */
@Module(includes = [CoreUiModule::class])
class CoreUiGeneralModule {
    // All general providers should be public.
    @Provides
    @Singleton
    internal fun provideCiceroneHolder(): ICiceroneHolder = CiceroneHolder
}