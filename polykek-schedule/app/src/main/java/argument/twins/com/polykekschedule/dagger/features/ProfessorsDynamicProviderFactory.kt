package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.feature.faq.dagger.FaqComponentHolder
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.feature.schedule.base.dagger.ScheduleComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder3
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.professors.base.dagger.IProfessorsModuleDependencies
import com.android.professors.base.dagger.IProfessorsNavigationActions
import com.android.professors.base.dagger.ProfessorsComponentHolder
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleControllerModuleApi
import com.android.schedule.controller.api.ITeacherToProfessorConvertor
import com.android.schedule.controller.impl.dagger.ScheduleControllerComponentHolder
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Professors dynamic provider factory. Injected class have to be part of app-core.
 *
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [ProfessorsDynamicProviderFactory]
 */
class ProfessorsDynamicProviderFactory @Inject constructor(
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : DynamicDependenciesProviderFactory<ProfessorsComponentHolder, IProfessorsModuleDependencies>(ProfessorsComponentHolder) {
    private class ProfessorsModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModule: ICoreRetrofitModuleApi,
        scheduleControllerModuleApi: IScheduleControllerModuleApi,
        override val block: (IBaseDependencyHolder<IProfessorsModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi, IScheduleControllerModuleApi) -> IProfessorsModuleDependencies
    ) : DependencyHolder3<ICoreUiModuleApi, ICoreRetrofitModuleApi, IScheduleControllerModuleApi, IProfessorsModuleDependencies>(
        coreUiModuleApi,
        coreRetrofitModule,
        scheduleControllerModuleApi
    )

    /**
     * Get professors navigation actions. Creates instances of components exactly by user navigation action.
     */
    private val professorsNavigationActions: IProfessorsNavigationActions = object : IProfessorsNavigationActions {
        override fun getMainScreen(): Fragment =
            MainScreenComponentHolder.getApi().getMainFragment()

        override fun getProfessorScheduleFragment(id: Int, title: String?): Fragment =
            ScheduleComponentHolder.getApi().getProfessorScheduleFragment(id, title)

        override fun getFaqFragment(): Fragment =
            FaqComponentHolder.getApi().getFaqFragment()
    }

    override fun getDynamicProvider(): DynamicProvider<IProfessorsModuleDependencies> = DynamicProvider {
        ProfessorsModuleDependenciesHolder(
            CoreUiComponentHolder.getApi(),
            CoreRetrofitComponentHolder.getApi(),
            ScheduleControllerComponentHolder.getApi()
        ) { dependencyHolder, coreUiModuleApi, coreRetrofitModuleApi, scheduleControllerModuleApi ->
            object : IProfessorsModuleDependencies {
                override val professorsNavigationActions: IProfessorsNavigationActions
                    get() = this@ProfessorsDynamicProviderFactory.professorsNavigationActions
                override val teacherToProfessorConvertor: ITeacherToProfessorConvertor
                    get() = scheduleControllerModuleApi.teacherToProfessorConvertor
                override val scheduleController: IScheduleController
                    get() = scheduleControllerModuleApi.scheduleController
                override val savedItemsRoomRepository: ISavedItemsRoomRepository
                    get() = this@ProfessorsDynamicProviderFactory.savedItemsRoomRepository
                override val retrofit: Retrofit
                    get() = coreRetrofitModuleApi.retrofit
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}