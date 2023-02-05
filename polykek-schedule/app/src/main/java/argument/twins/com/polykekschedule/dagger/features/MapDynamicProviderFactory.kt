package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.feature.buildings.dagger.BuildingsComponentHolder
import com.android.feature.map.dagger.IMapModuleDependencies
import com.android.feature.map.dagger.IMapNavigationActions
import com.android.feature.map.dagger.MapComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleControllerModuleApi
import com.android.schedule.controller.impl.dagger.ScheduleControllerComponentHolder
import javax.inject.Inject

/**
 * Map dynamic provider factory.
 *
 * @constructor Create empty constructor for map dynamic provider factory
 */
class MapDynamicProviderFactory @Inject constructor() :
    DynamicDependenciesProviderFactory<MapComponentHolder, IMapModuleDependencies>(MapComponentHolder) {
    private class MapModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        scheduleControllerModuleApi: IScheduleControllerModuleApi,
        override val block: (IBaseDependencyHolder<IMapModuleDependencies>, ICoreUiModuleApi, IScheduleControllerModuleApi) -> IMapModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, IScheduleControllerModuleApi, IMapModuleDependencies>(
        coreUiModuleApi,
        scheduleControllerModuleApi
    )

    /**
     * Get map navigation actions. We want to initialize Feedback api and module only if user clicks to getFeedbackScreen.
     */
    private val mapNavigationActions: IMapNavigationActions = object : IMapNavigationActions {
        override fun getBuildingsScreen(): Fragment = BuildingsComponentHolder.getApi().getBuildingsFragment()
    }

    override fun getDynamicProvider(): DynamicProvider<IMapModuleDependencies> = DynamicProvider {
        MapModuleDependenciesHolder(
            CoreUiComponentHolder.getApi(),
            ScheduleControllerComponentHolder.getApi()
        ) { dependencyHolder, coreUiModuleApi, scheduleControllerModuleApi ->
            object : IMapModuleDependencies {
                override val mapNavigationActions: IMapNavigationActions
                    get() = this@MapDynamicProviderFactory.mapNavigationActions
                override val scheduleController: IScheduleController
                    get() = scheduleControllerModuleApi.scheduleController
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}