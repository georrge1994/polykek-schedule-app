package argument.twins.com.polykekschedule.background

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BackgroundModule {
    @ContributesAndroidInjector
    internal abstract fun fcmServiceInjector(): PolytechFirebaseMessagingService
}