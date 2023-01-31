package argument.twins.com.polykekschedule

import android.app.Application
import argument.twins.com.polykekschedule.dagger.AppComponent
import argument.twins.com.polykekschedule.dagger.DaggerAppComponent
import argument.twins.com.polykekschedule.dagger.core.SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.schedule.controller.impl.dagger.IScheduleControllerModuleDependencies
import com.android.schedule.controller.impl.dagger.ScheduleControllerComponentHolder
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import javax.inject.Named

/**
 * App.
 *
 * @constructor Create empty constructor for app
 */
class App : Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    @Named(SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER)
    lateinit var dynamicDependencyProviderForScheduleController: DynamicProvider<IScheduleControllerModuleDependencies>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)

        // In the init block the ScheduleController requests a schedule for today. In the result the request will be sent before activity
        // and viewModels creation.
        ScheduleControllerComponentHolder.initAndGet(dynamicDependencyProviderForScheduleController).scheduleController.hashCode()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}