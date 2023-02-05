package argument.twins.com.polykekschedule.dagger.collector

import com.android.module.injector.dependenciesHolders.IDynamicDependenciesHolder
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.moduleMarkers.IModuleDependencies

/**
 * Dynamic dependencies provider factory promise to provide [DynamicProvider] for [IDynamicDependenciesHolder].
 * Every each implementation have to be added in [DynamicDependenciesProviderFactoryBinder].
 *
 * @param T Subtype of [IDynamicDependenciesHolder]
 * @param R Subtype of [IModuleDependencies] that holds [T]
 * @property componentHolder Instance of [T]
 * @constructor Create [DynamicDependenciesProviderFactory]
 */
abstract class DynamicDependenciesProviderFactory<T : IDynamicDependenciesHolder<R>, R : IModuleDependencies>(
    private val componentHolder: T
) : IDynamicDependenciesProviderFactory {
    override fun initDynamicDependenciesProvider() {
        componentHolder.dependenciesProvider = getDynamicProvider()
    }

    /**
     * Get dynamic provider.
     *
     * @return [DynamicProvider]
     */
    protected abstract fun getDynamicProvider(): DynamicProvider<R>
}