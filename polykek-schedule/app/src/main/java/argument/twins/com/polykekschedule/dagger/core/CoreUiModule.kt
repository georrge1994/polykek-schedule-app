package argument.twins.com.polykekschedule.dagger.core

import android.app.Application
import com.android.core.ui.dagger.*
import com.android.core.ui.navigation.ICiceroneHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder0
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Named
import javax.inject.Singleton


internal const val CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER = "CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER"

/**
 * Provides core UI dependencies in dynamic dependency provider form. We don't want to pass the same stuff for every each module.
 *
 * @constructor Create empty constructor for core ui module provider
 */
@Module
class CoreUiModule {
    private class CoreUiModuleDependenciesHolder(
        override val block: (IBaseDependencyHolder<ICoreUiModuleDependencies>) -> ICoreUiModuleDependencies
    ) : DependencyHolder0<ICoreUiModuleDependencies>()

    @Provides
    @Singleton
    @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForCoreUi(
        application: Application,
        @Named(BACKGROUND_MESSAGE_BUS) backgroundMessageBus: MutableSharedFlow<String>
    ) = DynamicProvider {
        CoreUiModuleDependenciesHolder { dependencyHolder ->
            object : ICoreUiModuleDependencies {
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = object : ICoreUiDependencies {
                        override val application: Application
                            get() = application
                        override val backgroundMessageBus: MutableSharedFlow<String>
                            get() = backgroundMessageBus
                    }
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}