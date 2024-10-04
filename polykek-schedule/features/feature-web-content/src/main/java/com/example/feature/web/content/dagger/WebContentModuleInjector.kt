package com.example.feature.web.content.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.example.feature.web.content.viewModels.WebContentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class WebContentModuleInjector {
    @Binds
    @IntoMap
    @ViewModelKey(WebContentViewModel::class)
    internal abstract fun bindWebContentViewModel(viewModel: WebContentViewModel): ViewModel
}