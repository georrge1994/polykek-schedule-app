package com.android.feature.notes.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.feature.notes.fragments.NoteEditorFragment
import com.android.feature.notes.fragments.NoteListFragment
import com.android.feature.notes.fragments.NotesFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [INotesModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        NotesModuleInjector::class
    ]
)
internal abstract class NotesComponent : IModuleComponent, INotesModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: NotesFragment)
    internal abstract fun inject(fragment: NoteListFragment)
    internal abstract fun inject(fragment: NoteEditorFragment)

    override fun getNotesFragment(): Fragment = NotesFragment()
    // Open note editor with different parameters.
    override fun getNoteEditorFragment(noteId: String, title: String): Fragment = NoteEditorFragment.newInstance(noteId, title)
    override fun getNoteEditorFragment(selectedItem: Int): Fragment = NoteEditorFragment.newInstance(selectedItem)
    override fun getNoteEditorFragment(ownNoteId: String): Fragment = NoteEditorFragment.newInstance(ownNoteId)

    companion object {
        /**
         * Create [NotesComponent].
         *
         * @param NotesModuleDependencies Notes module dependencies
         * @return [NotesComponent]
         */
        internal fun create(NotesModuleDependencies: INotesModuleDependencies): NotesComponent =
            DaggerNotesComponent.builder()
                .iNotesModuleDependencies(NotesModuleDependencies)
                .build()
    }
}