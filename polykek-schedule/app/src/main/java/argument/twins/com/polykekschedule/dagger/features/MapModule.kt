package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import argument.twins.com.polykekschedule.dagger.core.SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.feature.buildings.dagger.BuildingsComponentHolder
import com.android.feature.buildings.dagger.IBuildingsModuleDependencies
import com.android.feature.map.dagger.IMapModuleDependencies
import com.android.feature.map.dagger.IMapNavigationActions
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleControllerModuleApi
import com.android.schedule.controller.impl.dagger.IScheduleControllerModuleDependencies
import com.android.schedule.controller.impl.dagger.ScheduleControllerComponentHolder
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

internal const val MAP_DYNAMIC_DEPENDENCIES_PROVIDER = "MAP_DYNAMIC_DEPENDENCIES_PROVIDER"

@Module
class MapModule {
    private class MapModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        scheduleControllerModuleApi: IScheduleControllerModuleApi,
        override val block: (IBaseDependencyHolder<IMapModuleDependencies>, ICoreUiModuleApi, IScheduleControllerModuleApi) -> IMapModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, IScheduleControllerModuleApi, IMapModuleDependencies>(coreUiModuleApi, scheduleControllerModuleApi)

    @Provides
    @Singleton
    @Named(MAP_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForMap(
        @Named(SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForScheduleController: DynamicProvider<IScheduleControllerModuleDependencies>,
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>,
        @Named(BUILDINGS_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForBuildings: DynamicProvider<IBuildingsModuleDependencies>,
    ) = DynamicProvider {
        MapModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi),
            ScheduleControllerComponentHolder.initAndGet(dynamicDependencyProviderForScheduleController)
        ) { dependencyHolder, coreUiModuleApi, scheduleControllerModuleApi ->
            object : IMapModuleDependencies {
                override val mapNavigationActions: IMapNavigationActions
                    get() = object : IMapNavigationActions {
                        override fun getBuildingsScreen(): Fragment = BuildingsComponentHolder.initAndGet(dynamicDependencyProviderForBuildings).getBuildingsFragment()
                    }
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