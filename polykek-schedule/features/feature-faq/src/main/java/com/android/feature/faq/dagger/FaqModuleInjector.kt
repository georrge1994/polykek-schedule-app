package com.android.feature.faq.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.feature.faq.viewModels.FaqViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class FaqModuleInjector {
    @Binds
    @IntoMap
    @ViewModelKey(FaqViewModel::class)
    internal abstract fun bindFaqViewModel(viewModel: FaqViewModel): ViewModel
}