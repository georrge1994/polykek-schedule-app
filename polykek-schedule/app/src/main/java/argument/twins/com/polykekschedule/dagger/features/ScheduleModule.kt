package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import argument.twins.com.polykekschedule.dagger.core.SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.feature.notes.dagger.INotesModuleDependencies
import com.android.feature.notes.dagger.NotesComponentHolder
import com.android.feature.schedule.base.dagger.IScheduleModuleDependencies
import com.android.feature.schedule.base.dagger.IScheduleNavigationActions
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.schedule.controller.api.IProfessorScheduleUseCase
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleControllerModuleApi
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.schedule.controller.impl.dagger.IScheduleControllerModuleDependencies
import com.android.schedule.controller.impl.dagger.ScheduleControllerComponentHolder
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

internal const val SCHEDULE_DYNAMIC_DEPENDENCIES_PROVIDER = "SCHEDULE_DYNAMIC_DEPENDENCIES_PROVIDER"

@Module
class ScheduleModule {
    private class ScheduleModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        scheduleControllerModuleApi: IScheduleControllerModuleApi,
        override val block: (IBaseDependencyHolder<IScheduleModuleDependencies>, ICoreUiModuleApi, IScheduleControllerModuleApi) -> IScheduleModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, IScheduleControllerModuleApi, IScheduleModuleDependencies>(coreUiModuleApi, scheduleControllerModuleApi)

    @Provides
    @Singleton
    @Named(SCHEDULE_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForSchedule(
        @Named(SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForScheduleController: DynamicProvider<IScheduleControllerModuleDependencies>,
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>,
        @Named(NOTES_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForNotes: DynamicProvider<INotesModuleDependencies>
    ) = DynamicProvider {
        ScheduleModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi),
            ScheduleControllerComponentHolder.initAndGet(dynamicDependencyProviderForScheduleController)
        ) { dependencyHolder, coreUiModuleApi, scheduleControllerModuleApi ->
            object : IScheduleModuleDependencies {
                override val scheduleNavigationActions: IScheduleNavigationActions
                    get() = getScheduleNavigationActions(dynamicDependencyProviderForNotes)
                override val scheduleController: IScheduleController
                    get() = scheduleControllerModuleApi.scheduleController
                override val scheduleDateUseCase: IScheduleDateUseCase
                    get() = scheduleControllerModuleApi.scheduleDateUseCase
                override val professorScheduleUseCase: IProfessorScheduleUseCase
                    get() = scheduleControllerModuleApi.professorScheduleUseCase
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }

    /**
     * Get schedule navigation actions. Init note editor component only by user click.
     *
     * @param dynamicDependencyProviderForNotes Dynamic dependency provider for notes
     * @return [IScheduleNavigationActions]
     */
    private fun getScheduleNavigationActions(
        @Named(NOTES_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForNotes: DynamicProvider<INotesModuleDependencies>
    ): IScheduleNavigationActions = object : IScheduleNavigationActions {
        override fun getNoteEditorFragment(noteId: String, title: String): Fragment =
            NotesComponentHolder.initAndGet(dynamicDependencyProviderForNotes).getNoteEditorFragment(noteId, title)
    }
}