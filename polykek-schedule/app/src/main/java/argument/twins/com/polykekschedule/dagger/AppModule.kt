package argument.twins.com.polykekschedule.dagger

import android.app.Application
import argument.twins.com.polykekschedule.App
import argument.twins.com.polykekschedule.activity.dagger.ActivityModule
import argument.twins.com.polykekschedule.background.BackgroundModule
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactoryBinder
import argument.twins.com.polykekschedule.dagger.core.CoreRoomModule
import argument.twins.com.polykekschedule.dagger.features.*
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.core.ui.dagger.CoreUiGeneralModule
import com.android.core.ui.viewModels.ViewModelBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Named
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelBuilder::class,
        CoreUiGeneralModule::class,
        ActivityModule::class,
        BackgroundModule::class,
        CoreRoomModule::class,
        DynamicDependenciesProviderFactoryBinder::class
    ]
)
class AppModule {
    @Provides
    @Singleton
    fun provideApp(application: App): Application = application

    @Provides
    @Singleton
    @Named(BACKGROUND_MESSAGE_BUS)
    fun provideBackgroundMessageBus(): MutableSharedFlow<String> = MutableSharedFlow(replay = 1)
}