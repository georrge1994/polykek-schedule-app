package com.android.core.ui.dagger

import com.android.core.ui.viewModels.ViewModelBuilder
import dagger.Module

@Module(includes = [ViewModelBuilder::class])
internal abstract class CoreUiModule {
    // Here could be add core ui modules viewModels.
}