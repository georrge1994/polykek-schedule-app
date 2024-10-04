package argument.twins.com.polykekschedule.dagger.features

import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.feature.faq.dagger.FaqComponentHolder
import com.android.feature.faq.dagger.IFaqModuleDependencies
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import dagger.Module
import javax.inject.Inject

/**
 * Faq dynamic provider factory.
 *
 * @constructor Create empty constructor for faq dynamic provider factory
 */
@Module
class FaqDynamicProviderFactory @Inject constructor() :
    DynamicDependenciesProviderFactory<FaqComponentHolder, IFaqModuleDependencies>(FaqComponentHolder) {
    private class FaqModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<IFaqModuleDependencies>, ICoreUiModuleApi) -> IFaqModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, IFaqModuleDependencies>(coreUiModuleApi)

    override fun getDynamicProvider(): DynamicProvider<IFaqModuleDependencies> = DynamicProvider {
        FaqModuleDependenciesHolder(CoreUiComponentHolder.getApi()) { dependencyHolder, coreUiModuleApi ->
            object : IFaqModuleDependencies {
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}