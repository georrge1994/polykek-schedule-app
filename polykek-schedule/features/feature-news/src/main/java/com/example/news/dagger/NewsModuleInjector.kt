package com.example.news.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.example.news.viewModels.NewsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(
    includes = [
        NewsApiServiceProvider::class
    ]
)
internal abstract class NewsModuleInjector {
    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    internal abstract fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel
}