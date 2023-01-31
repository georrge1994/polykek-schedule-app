package com.android.core.ui.dagger

import android.app.Application
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Named

/**
 * Provides dependencies providers exactly for feature modules. App module contains an own implementations of these dependencies.
 *
 * @constructor Create empty constructor for core ui for features module
 */
@Module(includes = [CoreUiGeneralModule::class])
class CoreUiForFeatureModules {
    // All providers for base ui core module should be public.
    @Provides
    internal fun provideApplicationForUiCore(
        coreBaseUiDependencies: ICoreUiDependencies
    ): Application = coreBaseUiDependencies.application

    @Provides
    @Named(BACKGROUND_MESSAGE_BUS)
    internal fun provideBackgroundBusForUiCore(
        coreBaseUiDependencies: ICoreUiDependencies
    ): MutableSharedFlow<String> = coreBaseUiDependencies.backgroundMessageBus
}