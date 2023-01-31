package argument.twins.com.polykekschedule.dagger.features

import argument.twins.com.polykekschedule.dagger.core.CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER
import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.retrofit.impl.dagger.ICoreRetrofitDependencies
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.feature.buildings.dagger.IBuildingsModuleDependencies
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

internal const val BUILDINGS_DYNAMIC_DEPENDENCIES_PROVIDER = "BUILDINGS_DYNAMIC_DEPENDENCIES_PROVIDER"

@Module
class BuildingsModule {
    private class BuildingsModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModule: ICoreRetrofitModuleApi,
        override val block: (IBaseDependencyHolder<IBuildingsModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi) -> IBuildingsModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, ICoreRetrofitModuleApi, IBuildingsModuleDependencies>(coreUiModuleApi, coreRetrofitModule)

    @Provides
    @Singleton
    @Named(BUILDINGS_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForBuildings(
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>,
        @Named(CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreRetrofit: DynamicProvider<ICoreRetrofitDependencies>
    ) = DynamicProvider {
        BuildingsModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi),
            CoreRetrofitComponentHolder.initAndGet(dynamicDependencyProviderForCoreRetrofit)
        ) { dependencyHolder, coreUiModuleApi, coreRetrofitModuleApi ->
            object : IBuildingsModuleDependencies {
                override val retrofit: Retrofit
                    get() = coreRetrofitModuleApi.retrofit
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}