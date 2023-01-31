package com.android.feature.schedule.student.dagger

import androidx.lifecycle.ViewModel
import com.android.core.ui.viewModels.ViewModelKey
import com.android.feature.schedule.student.viewModels.ScheduleWeekViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class StudentScheduleModule {
    @Binds
    @IntoMap
    @ViewModelKey(ScheduleWeekViewModel::class)
    internal abstract fun bindScheduleViewModel(viewModel: ScheduleWeekViewModel): ViewModel
}