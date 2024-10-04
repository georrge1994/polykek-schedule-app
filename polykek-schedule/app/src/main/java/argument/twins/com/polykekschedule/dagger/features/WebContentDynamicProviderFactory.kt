package argument.twins.com.polykekschedule.dagger.features

import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.example.feature.web.content.dagger.IWebContentModuleDependencies
import com.example.feature.web.content.dagger.WebContentComponentHolder
import dagger.Module
import javax.inject.Inject

/**
 * Web content dynamic provider factory.
 * 
 * @constructor Create empty Web content dynamic provider factory
 */
@Module
class WebContentDynamicProviderFactory @Inject constructor() :
    DynamicDependenciesProviderFactory<WebContentComponentHolder, IWebContentModuleDependencies>(WebContentComponentHolder) {
    private class WebContentModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<IWebContentModuleDependencies>, ICoreUiModuleApi) -> IWebContentModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, IWebContentModuleDependencies>(coreUiModuleApi)

    override fun getDynamicProvider(): DynamicProvider<IWebContentModuleDependencies> = DynamicProvider {
        WebContentModuleDependenciesHolder(CoreUiComponentHolder.getApi()) { dependencyHolder, coreUiModuleApi ->
            object : IWebContentModuleDependencies {
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}