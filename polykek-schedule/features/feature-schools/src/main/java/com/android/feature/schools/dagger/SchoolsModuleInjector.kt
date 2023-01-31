package com.android.feature.schools.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.feature.schools.viewModels.SchoolViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [SchoolApiServiceProvider::class])
internal abstract class SchoolsModuleInjector {
    @Binds
    @IntoMap
    @ViewModelKey(SchoolViewModel::class)
    internal abstract fun bindSchoolViewModel(viewModel: SchoolViewModel): ViewModel
}