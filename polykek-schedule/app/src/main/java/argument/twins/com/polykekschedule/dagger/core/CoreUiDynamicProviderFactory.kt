package argument.twins.com.polykekschedule.dagger.core

import android.app.Application
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.ui.dagger.*
import com.android.module.injector.dependenciesHolders.DependencyHolder0
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Named

/**
 * Provides core UI dependencies in dynamic dependency provider form. We don't want to pass the same stuff for every each module.
 *
 * @constructor Create empty constructor for core ui module provider
 */
class CoreUiDynamicProviderFactory @Inject constructor(
    private val application: Application,
    @Named(BACKGROUND_MESSAGE_BUS) private val backgroundMessageBus: MutableSharedFlow<String>
) : DynamicDependenciesProviderFactory<CoreUiComponentHolder, ICoreUiModuleDependencies>(CoreUiComponentHolder) {
    private class CoreUiModuleDependenciesHolder(
        override val block: (IBaseDependencyHolder<ICoreUiModuleDependencies>) -> ICoreUiModuleDependencies
    ) : DependencyHolder0<ICoreUiModuleDependencies>()

    override fun getDynamicProvider(): DynamicProvider<ICoreUiModuleDependencies> = DynamicProvider {
        CoreUiModuleDependenciesHolder { dependencyHolder ->
            object : ICoreUiModuleDependencies {
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = object : ICoreUiDependencies {
                        override val application: Application
                            get() = this@CoreUiDynamicProviderFactory.application
                        override val backgroundMessageBus: MutableSharedFlow<String>
                            get() = this@CoreUiDynamicProviderFactory.backgroundMessageBus
                    }
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}