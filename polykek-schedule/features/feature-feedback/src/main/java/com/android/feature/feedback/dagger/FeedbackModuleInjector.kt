package com.android.feature.feedback.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.feature.feedback.viewModels.FeedbackViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class FeedbackModuleInjector {
    @Binds
    @IntoMap
    @ViewModelKey(FeedbackViewModel::class)
    internal abstract fun bindFeedbackViewModel(viewModel: FeedbackViewModel): ViewModel
}