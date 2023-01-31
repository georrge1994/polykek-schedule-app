package argument.twins.com.polykekschedule.activity.dagger

import androidx.lifecycle.ViewModel
import argument.twins.com.polykekschedule.activity.viewModels.MainActivityViewModel
import argument.twins.com.polykekschedule.activity.viewModels.NotificationViewModel
import com.android.core.ui.viewModels.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    internal abstract fun bindNotificationViewModel(viewModel: NotificationViewModel): ViewModel
}