package com.android.core.retrofit.impl.dagger

import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.retrofit.RetrofitProviderModule
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [ICoreRetrofitDependencies::class],
    modules = [
        RetrofitProviderModule::class
    ]
)
internal abstract class CoreRetrofitComponent : IModuleComponent, ICoreRetrofitModuleApi {
    companion object {
        /**
         * Create [CoreRetrofitComponent].
         *
         * @param iCoreRetrofitDependencies Core database dependencies
         * @return [CoreRetrofitComponent]
         */
        internal fun create(iCoreRetrofitDependencies: ICoreRetrofitDependencies): CoreRetrofitComponent =
            DaggerCoreRetrofitComponent.builder()
                .iCoreRetrofitDependencies(iCoreRetrofitDependencies)
                .build()
    }
}