package com.android.feature.buildings.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.feature.buildings.viewModels.BuildingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [BuildingApiServiceProvider::class])
internal abstract class BuildingsModuleInjector {
    @Binds
    @IntoMap
    @ViewModelKey(BuildingViewModel::class)
    internal abstract fun bindBuildingViewModel(viewModel: BuildingViewModel): ViewModel
}