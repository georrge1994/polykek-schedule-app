package com.android.core.ui.dagger

import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [ICoreUiModuleDependencies::class],
    modules = [CoreUiForFeatureModules::class]
)
internal abstract class CoreUiComponent : IModuleComponent, ICoreUiModuleApi {
    internal companion object {
        /**
         * Create [CoreUiComponent].
         *
         * @param uiModuleDependencies Base core ui module dependencies
         * @return [CoreUiComponent]
         */
        internal fun create(uiModuleDependencies: ICoreUiModuleDependencies): CoreUiComponent =
            DaggerCoreUiComponent.builder()
                .iCoreUiModuleDependencies(uiModuleDependencies)
                .build()
    }
}