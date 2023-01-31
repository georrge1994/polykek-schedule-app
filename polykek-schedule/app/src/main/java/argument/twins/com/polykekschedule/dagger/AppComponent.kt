package argument.twins.com.polykekschedule.dagger

import argument.twins.com.polykekschedule.App
import argument.twins.com.polykekschedule.activity.MainActivity
import argument.twins.com.polykekschedule.activity.fragments.NotificationDialogFragment
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    override fun inject(app: App)

    // Local feature screens.
    fun inject(fragment: NotificationDialogFragment)
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }
}