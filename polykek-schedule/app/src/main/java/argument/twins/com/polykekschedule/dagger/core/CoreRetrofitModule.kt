package argument.twins.com.polykekschedule.dagger.core

import android.app.Application
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.retrofit.impl.dagger.ICoreRetrofitDependencies
import com.android.module.injector.dependenciesHolders.DependencyHolder0
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

internal const val CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER = "CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER"

/**
 * Core retrofit module.
 *
 * @constructor Create empty constructor for core retrofit module
 */
@Module
class CoreRetrofitModule {
    private class CoreRetrofitDependencyHolder(
        override val block: (IBaseDependencyHolder<ICoreRetrofitDependencies>) -> ICoreRetrofitDependencies
    ) : DependencyHolder0<ICoreRetrofitDependencies>()

    @Provides
    @Singleton
    @Named(CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideCoreRetrofitDynamicDependenciesProvider(application: Application): DynamicProvider<ICoreRetrofitDependencies> = DynamicProvider {
        CoreRetrofitDependencyHolder { dependencyHolder ->
            object : ICoreRetrofitDependencies {
                override val application: Application
                    get() = application
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}