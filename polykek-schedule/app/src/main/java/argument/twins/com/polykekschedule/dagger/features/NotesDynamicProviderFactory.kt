package argument.twins.com.polykekschedule.dagger.features

import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import argument.twins.com.polykekschedule.room.notes.NotesRoomRepository
import argument.twins.com.polykekschedule.room.savedItems.SavedItemsRoomRepository
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.feature.notes.dagger.INotesModuleDependencies
import com.android.feature.notes.dagger.NotesComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import javax.inject.Inject

/**
 * Notes dynamic provider factory. Injected class have to be part of app-core.
 *
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @property notesRoomRepository Notes room repository to work with notes in the memory
 * @constructor Create [NotesDynamicProviderFactory]
 */
class NotesDynamicProviderFactory @Inject constructor(
    private val savedItemsRoomRepository: SavedItemsRoomRepository,
    private val notesRoomRepository: NotesRoomRepository
) : DynamicDependenciesProviderFactory<NotesComponentHolder, INotesModuleDependencies>(NotesComponentHolder) {
    private class NotesModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<INotesModuleDependencies>, ICoreUiModuleApi) -> INotesModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, INotesModuleDependencies>(coreUiModuleApi)

    override fun getDynamicProvider(): DynamicProvider<INotesModuleDependencies> = DynamicProvider {
        NotesModuleDependenciesHolder(CoreUiComponentHolder.getApi()) { dependencyHolder, coreUiModuleApi ->
            object : INotesModuleDependencies {
                override val notesRoomRepository: INotesRoomRepository
                    get() = this@NotesDynamicProviderFactory.notesRoomRepository
                override val savedItemsRoomRepository: ISavedItemsRoomRepository
                    get() = this@NotesDynamicProviderFactory.savedItemsRoomRepository
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}