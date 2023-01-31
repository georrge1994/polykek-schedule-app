package com.android.professors.search.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.professors.search.viewModels.ProfessorSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ProfessorsSearchModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProfessorSearchViewModel::class)
    internal abstract fun bindProfessorSearchViewModel(viewModel: ProfessorSearchViewModel): ViewModel
}