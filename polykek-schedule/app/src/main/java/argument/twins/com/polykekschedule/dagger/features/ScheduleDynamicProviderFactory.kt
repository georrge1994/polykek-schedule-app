package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.feature.notes.dagger.NotesComponentHolder
import com.android.feature.schedule.base.dagger.IScheduleModuleDependencies
import com.android.feature.schedule.base.dagger.IScheduleNavigationActions
import com.android.feature.schedule.base.dagger.ScheduleComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.schedule.controller.api.IProfessorScheduleUseCase
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleControllerModuleApi
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.schedule.controller.impl.dagger.ScheduleControllerComponentHolder
import javax.inject.Inject

/**
 * Schedule dynamic provider factory.
 *
 * @constructor Create empty constructor for schedule dynamic provider factory
 */
class ScheduleDynamicProviderFactory @Inject constructor() :
    DynamicDependenciesProviderFactory<ScheduleComponentHolder, IScheduleModuleDependencies>(ScheduleComponentHolder) {
    private class ScheduleModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        scheduleControllerModuleApi: IScheduleControllerModuleApi,
        override val block: (IBaseDependencyHolder<IScheduleModuleDependencies>, ICoreUiModuleApi, IScheduleControllerModuleApi) -> IScheduleModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, IScheduleControllerModuleApi, IScheduleModuleDependencies>(
        coreUiModuleApi,
        scheduleControllerModuleApi
    )

    /**
     * Get schedule navigation actions. Init note editor component only by user click.
     */
    private val scheduleNavigationActions: IScheduleNavigationActions = object : IScheduleNavigationActions {
        override fun getNoteEditorFragment(noteId: String, title: String): Fragment =
            NotesComponentHolder.getApi().getNoteEditorFragment(noteId, title)
    }

    override fun getDynamicProvider(): DynamicProvider<IScheduleModuleDependencies> = DynamicProvider {
        ScheduleModuleDependenciesHolder(
            CoreUiComponentHolder.getApi(),
            ScheduleControllerComponentHolder.getApi()
        ) { dependencyHolder, coreUiModuleApi, scheduleControllerModuleApi ->
            object : IScheduleModuleDependencies {
                override val scheduleNavigationActions: IScheduleNavigationActions
                    get() = this@ScheduleDynamicProviderFactory.scheduleNavigationActions
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
}