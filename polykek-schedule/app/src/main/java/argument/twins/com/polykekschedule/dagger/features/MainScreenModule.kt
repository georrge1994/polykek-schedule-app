package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import argument.twins.com.polykekschedule.room.savedItems.SavedItemsRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.feature.main.screen.dagger.IMainScreenModuleDependencies
import com.android.feature.main.screen.dagger.IMainScreenNavigationActions
import com.android.feature.map.dagger.IMapModuleDependencies
import com.android.feature.map.dagger.MapComponentHolder
import com.android.feature.notes.dagger.INotesModuleDependencies
import com.android.feature.notes.dagger.NotesComponentHolder
import com.android.feature.schedule.base.dagger.IScheduleModuleDependencies
import com.android.feature.schedule.base.dagger.ScheduleComponentHolder
import com.android.feature.schools.dagger.ISchoolsModuleDependencies
import com.android.feature.schools.dagger.SchoolsComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.professors.base.dagger.IProfessorsModuleDependencies
import com.android.professors.base.dagger.ProfessorsComponentHolder
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

internal const val MAIN_SCREEN_DYNAMIC_DEPENDENCIES_PROVIDER = "MAIN_SCREEN_DYNAMIC_DEPENDENCIES_PROVIDER"

/**
 * Main screen module.
 *
 * @constructor Create empty constructor for main screen module
 */
@Module
class MainScreenModule {
    private class MainScreenModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<IMainScreenModuleDependencies>, ICoreUiModuleApi) -> IMainScreenModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, IMainScreenModuleDependencies>(coreUiModuleApi)

    @Provides
    @Singleton
    @Named(MAIN_SCREEN_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForMainScreen(
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: dagger.Lazy<DynamicProvider<ICoreUiModuleDependencies>>,
        @Named(SCHEDULE_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForSchedule: dagger.Lazy<DynamicProvider<IScheduleModuleDependencies>>,
        @Named(NOTES_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForNotes: dagger.Lazy<DynamicProvider<INotesModuleDependencies>>,
        @Named(MAP_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForMap: dagger.Lazy<DynamicProvider<IMapModuleDependencies>>,
        @Named(PROFESSORS_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForProfessors: dagger.Lazy<DynamicProvider<IProfessorsModuleDependencies>>,
        @Named(SCHOOLS_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForSchools: dagger.Lazy<DynamicProvider<ISchoolsModuleDependencies>>,
        savedItemsRoomRepository: SavedItemsRoomRepository  // There is from app module.
    ) = DynamicProvider {
        MainScreenModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi.get())
        ) { dependencyHolder, coreUiModuleApi ->
            object : IMainScreenModuleDependencies {
                override val mainScreenNavigationActions: IMainScreenNavigationActions
                    get() = getIMainScreenNavigationActions(
                        dynamicDependencyProviderForSchedule.get(),
                        dynamicDependencyProviderForNotes.get(),
                        dynamicDependencyProviderForMap.get(),
                        dynamicDependencyProviderForProfessors.get(),
                        dynamicDependencyProviderForSchools.get()
                    )
                override val savedItemsRoomRepository: ISavedItemsRoomRepository
                    get() = savedItemsRoomRepository
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }

    /**
     * Get main screen navigation actions. Creates component-api instance only by navigation action!
     *
     * @param dynamicDependencyProviderForSchedule Dynamic dependency provider for schedule
     * @param dynamicDependencyProviderForNotes Dynamic dependency provider for notes
     * @param dynamicDependencyProviderForMap Dynamic dependency provider for map
     * @param dynamicDependencyProviderForProfessors Dynamic dependency provider for professors
     * @param dynamicDependencyProviderForSchools Dynamic dependency provider for schools
     * @return [IMainScreenNavigationActions]
     */
    private fun getIMainScreenNavigationActions(
        @Named(SCHEDULE_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForSchedule: DynamicProvider<IScheduleModuleDependencies>,
        @Named(NOTES_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForNotes: DynamicProvider<INotesModuleDependencies>,
        @Named(MAP_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForMap: DynamicProvider<IMapModuleDependencies>,
        @Named(PROFESSORS_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForProfessors: DynamicProvider<IProfessorsModuleDependencies>,
        @Named(SCHOOLS_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForSchools: DynamicProvider<ISchoolsModuleDependencies>
    ): IMainScreenNavigationActions = object : IMainScreenNavigationActions {
        override fun getScheduleFragment(): Fragment =
            ScheduleComponentHolder.initAndGet(dynamicDependencyProviderForSchedule).getScheduleWeekFragment()

        override fun getNotesFragment(): Fragment =
            NotesComponentHolder.initAndGet(dynamicDependencyProviderForNotes).getNotesFragment()

        override fun getMapFragment(): Fragment =
            MapComponentHolder.initAndGet(dynamicDependencyProviderForMap).getMapFragment()

        override fun getProfessorFragment(): Fragment =
            ProfessorsComponentHolder.initAndGet(dynamicDependencyProviderForProfessors).getProfessorsFragment()

        override fun getSchoolsFragment(): Fragment =
            SchoolsComponentHolder.initAndGet(dynamicDependencyProviderForSchools).getSchoolsFragment()

        override fun getProfessorSearchFragment(): Fragment =
            ProfessorsComponentHolder.initAndGet(dynamicDependencyProviderForProfessors).getProfessorSearchFragment()
    }
}