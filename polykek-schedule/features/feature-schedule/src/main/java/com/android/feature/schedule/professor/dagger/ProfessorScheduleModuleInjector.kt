package com.android.feature.schedule.professor.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.feature.schedule.professor.viewModels.ProfessorsScheduleViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ProfessorScheduleModuleInjector {
    @Binds
    @IntoMap
    @ViewModelKey(ProfessorsScheduleViewModel::class)
    internal abstract fun bindProfessorsScheduleViewModel(viewModel: ProfessorsScheduleViewModel): ViewModel
}