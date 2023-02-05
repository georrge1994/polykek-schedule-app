package argument.twins.com.polykekschedule.dagger.features

import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.feature.buildings.dagger.BuildingsComponentHolder
import com.android.feature.buildings.dagger.IBuildingsModuleDependencies
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Buildings dynamic provider factory.
 *
 * @constructor Create empty constructor for buildings dynamic provider factory
 */
class BuildingsDynamicProviderFactory @Inject constructor() :
    DynamicDependenciesProviderFactory<BuildingsComponentHolder, IBuildingsModuleDependencies>(BuildingsComponentHolder) {
    private class BuildingsModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModule: ICoreRetrofitModuleApi,
        override val block: (IBaseDependencyHolder<IBuildingsModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi) -> IBuildingsModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, ICoreRetrofitModuleApi, IBuildingsModuleDependencies>(coreUiModuleApi, coreRetrofitModule)

    override fun getDynamicProvider(): DynamicProvider<IBuildingsModuleDependencies> = DynamicProvider {
        BuildingsModuleDependenciesHolder(
            CoreUiComponentHolder.getApi(),
            CoreRetrofitComponentHolder.getApi()
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