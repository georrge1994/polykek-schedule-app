package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.core.CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER
import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import argument.twins.com.polykekschedule.dagger.core.SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.retrofit.impl.dagger.ICoreRetrofitDependencies
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.feature.faq.dagger.FaqComponentHolder
import com.android.feature.faq.dagger.IFaqModuleDependencies
import com.android.feature.main.screen.dagger.IMainScreenModuleDependencies
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.feature.schedule.base.dagger.IScheduleModuleDependencies
import com.android.feature.schedule.base.dagger.ScheduleComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder3
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.professors.base.dagger.IProfessorsModuleDependencies
import com.android.professors.base.dagger.IProfessorsNavigationActions
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleControllerModuleApi
import com.android.schedule.controller.api.ITeacherToProfessorConvertor
import com.android.schedule.controller.impl.dagger.IScheduleControllerModuleDependencies
import com.android.schedule.controller.impl.dagger.ScheduleControllerComponentHolder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

internal const val PROFESSORS_DYNAMIC_DEPENDENCIES_PROVIDER = "PROFESSORS_DYNAMIC_DEPENDENCIES_PROVIDER"

/**
 * Professors module.
 *
 * @constructor Create empty constructor for professors module
 */
@Module
class ProfessorsModule {
    private class ProfessorsModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModule: ICoreRetrofitModuleApi,
        scheduleControllerModuleApi: IScheduleControllerModuleApi,
        override val block: (IBaseDependencyHolder<IProfessorsModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi, IScheduleControllerModuleApi) -> IProfessorsModuleDependencies
    ) : DependencyHolder3<ICoreUiModuleApi, ICoreRetrofitModuleApi, IScheduleControllerModuleApi, IProfessorsModuleDependencies>(coreUiModuleApi, coreRetrofitModule, scheduleControllerModuleApi)

    @Provides
    @Singleton
    @Named(PROFESSORS_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForProfessors(
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>,
        @Named(CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreRetrofit: DynamicProvider<ICoreRetrofitDependencies>,
        @Named(SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForScheduleController: DynamicProvider<IScheduleControllerModuleDependencies>,
        @Named(FAQ_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForFaq: DynamicProvider<IFaqModuleDependencies>,
        @Named(SCHEDULE_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForSchedule: DynamicProvider<IScheduleModuleDependencies>,
        @Named(MAIN_SCREEN_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForMainScreen: dagger.Lazy<DynamicProvider<IMainScreenModuleDependencies>>,
        savedItemsRoomRepository: ISavedItemsRoomRepository
    ) = DynamicProvider {
        ProfessorsModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi),
            CoreRetrofitComponentHolder.initAndGet(dynamicDependencyProviderForCoreRetrofit),
            ScheduleControllerComponentHolder.initAndGet(dynamicDependencyProviderForScheduleController)
        ) { dependencyHolder, coreUiModuleApi, coreRetrofitModuleApi, scheduleControllerModuleApi ->
            object : IProfessorsModuleDependencies {
                override val professorsNavigationActions: IProfessorsNavigationActions
                    get() = getProfessorsNavigationActions(
                        dynamicDependencyProviderForFaq,
                        dynamicDependencyProviderForSchedule,
                        dynamicDependencyProviderForMainScreen.get()
                    )
                override val teacherToProfessorConvertor: ITeacherToProfessorConvertor
                    get() = scheduleControllerModuleApi.teacherToProfessorConvertor
                override val scheduleController: IScheduleController
                    get() = scheduleControllerModuleApi.scheduleController
                override val savedItemsRoomRepository: ISavedItemsRoomRepository
                    get() = savedItemsRoomRepository
                override val retrofit: Retrofit
                    get() = coreRetrofitModuleApi.retrofit
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }

    /**
     * Get professors navigation actions. Creates instances of components exactly by user navigation action.
     *
     * @param dynamicDependencyProviderForFaq Dynamic dependency provider for faq
     * @param dynamicDependencyProviderForSchedule Dynamic dependency provider for schedule
     * @param dynamicDependencyProviderForMainScreen Dynamic dependency provider for main screen
     * @return [IProfessorsNavigationActions]
     */
    private fun getProfessorsNavigationActions(
        @Named(FAQ_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForFaq: DynamicProvider<IFaqModuleDependencies>,
        @Named(SCHEDULE_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForSchedule: DynamicProvider<IScheduleModuleDependencies>,
        @Named(MAIN_SCREEN_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForMainScreen: DynamicProvider<IMainScreenModuleDependencies>
    ) = object : IProfessorsNavigationActions {
        override fun getMainScreen(): Fragment =
            MainScreenComponentHolder.initAndGet(dynamicDependencyProviderForMainScreen).getMainFragment()

        override fun getProfessorScheduleFragment(id: Int, title: String?): Fragment =
            ScheduleComponentHolder.initAndGet(dynamicDependencyProviderForSchedule).getProfessorScheduleFragment(id, title)

        override fun getFaqFragment(): Fragment = FaqComponentHolder.initAndGet(dynamicDependencyProviderForFaq).getFaqFragment()
    }
}