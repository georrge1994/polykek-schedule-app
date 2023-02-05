package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import argument.twins.com.polykekschedule.room.savedItems.SavedItemsRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.feature.main.screen.dagger.IMainScreenModuleDependencies
import com.android.feature.main.screen.dagger.IMainScreenNavigationActions
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.feature.map.dagger.MapComponentHolder
import com.android.feature.notes.dagger.NotesComponentHolder
import com.android.feature.schedule.base.dagger.ScheduleComponentHolder
import com.android.feature.schools.dagger.SchoolsComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.professors.base.dagger.ProfessorsComponentHolder
import javax.inject.Inject

/**
 * Main screen dynamic provider factory. Injected class have to be part of app-core.
 *
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [MainScreenDynamicProviderFactory]
 */
class MainScreenDynamicProviderFactory @Inject constructor(
    private val savedItemsRoomRepository: SavedItemsRoomRepository
) : DynamicDependenciesProviderFactory<MainScreenComponentHolder, IMainScreenModuleDependencies>(MainScreenComponentHolder) {
    private class MainScreenModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<IMainScreenModuleDependencies>, ICoreUiModuleApi) -> IMainScreenModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, IMainScreenModuleDependencies>(coreUiModuleApi)

    /**
     * Get main screen navigation actions. Creates component-api instance only by navigation action!
     */
    private val mainScreenNavigationActions = object : IMainScreenNavigationActions {
        override fun getScheduleFragment(): Fragment = ScheduleComponentHolder.getApi().getScheduleWeekFragment()

        override fun getNotesFragment(): Fragment = NotesComponentHolder.getApi().getNotesFragment()

        override fun getMapFragment(): Fragment = MapComponentHolder.getApi().getMapFragment()

        override fun getProfessorFragment(): Fragment = ProfessorsComponentHolder.getApi().getProfessorsFragment()

        override fun getSchoolsFragment(): Fragment = SchoolsComponentHolder.getApi().getSchoolsFragment()

        override fun getProfessorSearchFragment(): Fragment = ProfessorsComponentHolder.getApi().getProfessorSearchFragment()
    }

    override fun getDynamicProvider(): DynamicProvider<IMainScreenModuleDependencies> = DynamicProvider {
        MainScreenModuleDependenciesHolder(CoreUiComponentHolder.getApi()) { dependencyHolder, coreUiModuleApi ->
            object : IMainScreenModuleDependencies {
                override val mainScreenNavigationActions: IMainScreenNavigationActions
                    get() = this@MainScreenDynamicProviderFactory.mainScreenNavigationActions
                override val savedItemsRoomRepository: ISavedItemsRoomRepository
                    get() = this@MainScreenDynamicProviderFactory.savedItemsRoomRepository
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}