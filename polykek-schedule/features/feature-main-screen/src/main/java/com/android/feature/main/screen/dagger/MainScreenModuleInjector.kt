package com.android.feature.main.screen.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.feature.main.screen.menu.viewModels.BottomAnimationViewModel
import com.android.feature.main.screen.saved.viewModels.SavedItemsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class MainScreenModuleInjector {
    @Binds
    @IntoMap
    @ViewModelKey(BottomAnimationViewModel::class)
    internal abstract fun bindBottomAnimationViewModel(viewModel: BottomAnimationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedItemsViewModel::class)
    internal abstract fun bindSavedItemsViewModel(viewModel: SavedItemsViewModel): ViewModel
}