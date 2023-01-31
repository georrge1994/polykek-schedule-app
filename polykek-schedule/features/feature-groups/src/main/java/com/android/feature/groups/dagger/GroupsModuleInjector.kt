package com.android.feature.groups.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.feature.groups.viewModels.GroupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [GroupApiServiceProvider::class])
internal abstract class GroupsModuleInjector {
    @Binds
    @IntoMap
    @ViewModelKey(GroupViewModel::class)
    internal abstract fun bindGroupViewModel(viewModel: GroupViewModel): ViewModel
}