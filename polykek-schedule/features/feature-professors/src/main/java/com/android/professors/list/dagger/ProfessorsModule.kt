package com.android.professors.list.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.professors.list.viewModels.ProfessorsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ProfessorsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProfessorsViewModel::class)
    internal abstract fun bindProfessorsViewModel(viewModel: ProfessorsViewModel): ViewModel
}