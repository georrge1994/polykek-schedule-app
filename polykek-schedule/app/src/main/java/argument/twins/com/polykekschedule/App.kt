package argument.twins.com.polykekschedule

import android.app.Application
import argument.twins.com.polykekschedule.dagger.AppComponent
import argument.twins.com.polykekschedule.dagger.DaggerAppComponent
import argument.twins.com.polykekschedule.dagger.collector.IDynamicDependenciesProviderFactory
import com.android.schedule.controller.impl.dagger.ScheduleControllerComponentHolder
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * App.
 *
 * @constructor Create empty constructor for app
 */
class App : Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var dynamicDependenciesProviderFactories: Set<@JvmSuppressWildcards IDynamicDependenciesProviderFactory>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        // Init all dynamic dependencies providers.
        dynamicDependenciesProviderFactories.forEach { it.initDynamicDependenciesProvider() }
        // In the init block the ScheduleController requests a schedule for today. In the result the request will be sent before activity
        // and viewModels creation.
        ScheduleControllerComponentHolder.getApi().scheduleController.hashCode()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}