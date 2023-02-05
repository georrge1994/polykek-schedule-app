package argument.twins.com.polykekschedule.dagger.core

import android.app.Application
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.retrofit.impl.dagger.ICoreRetrofitDependencies
import com.android.module.injector.dependenciesHolders.DependencyHolder0
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import javax.inject.Inject

/**
 * Core retrofit dynamic provider factory.
 *
 * @property application Application object to get context
 * @constructor Create [CoreRetrofitDynamicProviderFactory]
 */
class CoreRetrofitDynamicProviderFactory @Inject constructor(
    private val application: Application
) : DynamicDependenciesProviderFactory<CoreRetrofitComponentHolder, ICoreRetrofitDependencies>(CoreRetrofitComponentHolder) {
    private class CoreRetrofitDependencyHolder(
        override val block: (IBaseDependencyHolder<ICoreRetrofitDependencies>) -> ICoreRetrofitDependencies
    ) : DependencyHolder0<ICoreRetrofitDependencies>()

    override fun getDynamicProvider(): DynamicProvider<ICoreRetrofitDependencies> = DynamicProvider {
        CoreRetrofitDependencyHolder { dependencyHolder ->
            object : ICoreRetrofitDependencies {
                override val application: Application
                    get() = this@CoreRetrofitDynamicProviderFactory.application
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}