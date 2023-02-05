package argument.twins.com.polykekschedule.dagger.collector

/**
 * Dynamic dependencies provider factory promise to init dependencyDynamicHolder in ComponentHolder.
 * Every each implementation have to be added in [DynamicDependenciesProviderFactoryBinder].
 */
interface IDynamicDependenciesProviderFactory {
    /**
     * Init dynamic dependencies provider.
     */
    fun initDynamicDependenciesProvider()
}