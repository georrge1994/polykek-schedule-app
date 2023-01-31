package argument.twins.com.polykekschedule.dagger.features

import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import argument.twins.com.polykekschedule.room.notes.NotesRoomRepository
import argument.twins.com.polykekschedule.room.savedItems.SavedItemsRoomRepository
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.feature.notes.dagger.INotesModuleDependencies
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

internal const val NOTES_DYNAMIC_DEPENDENCIES_PROVIDER = "NOTES_DYNAMIC_DEPENDENCIES_PROVIDER"

@Module
class NotesModule {
    private class NotesModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<INotesModuleDependencies>, ICoreUiModuleApi) -> INotesModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, INotesModuleDependencies>(coreUiModuleApi)

    @Provides
    @Singleton
    @Named(NOTES_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForNotes(
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>,
        savedItemsRoomRepository: SavedItemsRoomRepository, // There are from app module.
        notesRoomRepository: NotesRoomRepository
    ) = DynamicProvider {
        NotesModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi)
        ) { dependencyHolder, coreUiModuleApi ->
            object : INotesModuleDependencies {
                override val notesRoomRepository: INotesRoomRepository
                    get() = notesRoomRepository
                override val savedItemsRoomRepository: ISavedItemsRoomRepository
                    get() = savedItemsRoomRepository
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}