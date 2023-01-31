package com.android.feature.notes.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.feature.notes.viewModels.NoteEditorViewModel
import com.android.feature.notes.viewModels.NotesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class NotesModuleInjector {
    @Binds
    @IntoMap
    @ViewModelKey(NotesViewModel::class)
    internal abstract fun bindNotesViewModel(viewModel: NotesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteEditorViewModel::class)
    internal abstract fun bindNoteEditorViewModel(editorViewModel: NoteEditorViewModel): ViewModel
}