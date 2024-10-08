package argument.twins.com.polykekschedule.dagger.collector

import argument.twins.com.polykekschedule.dagger.core.CoreRetrofitDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.core.CoreUiDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.core.ScheduleControllerDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.BuildingsDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.FaqDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.GroupsDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.MainScreenDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.MapDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.NewsDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.NotesDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.ProfessorsDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.ScheduleDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.SchoolsDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.WebContentDynamicProviderFactory
import argument.twins.com.polykekschedule.dagger.features.WelcomeDynamicProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

/**
 * Dynamic dependencies provider factory binder.
 *
 * @constructor Create empty constructor for dynamic dependencies provider factory binder
 */
@Module
abstract class DynamicDependenciesProviderFactoryBinder {
    // Core.
    @Binds
    @IntoSet
    abstract fun bindScheduleControllerDynamicProviderFactory(factory: ScheduleControllerDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindCoreRetrofitDynamicProviderFactory(factory: CoreRetrofitDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindCoreUiDynamicProviderFactory(factory: CoreUiDynamicProviderFactory): IDynamicDependenciesProviderFactory

    // Features.
    @Binds
    @IntoSet
    abstract fun bindBuildingsDynamicProviderFactory(factory: BuildingsDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindFaqDynamicProviderFactory(factory: FaqDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindGroupsDynamicProviderFactory(factory: GroupsDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindMainScreenDynamicProviderFactory(factory: MainScreenDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindMapDynamicProviderFactory(factory: MapDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindNotesDynamicProviderFactory(factory: NotesDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindProfessorsDynamicProviderFactory(factory: ProfessorsDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindScheduleDynamicProviderFactory(factory: ScheduleDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindSchoolsDynamicProviderFactory(factory: SchoolsDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindWelcomeDynamicProviderFactory(factory: WelcomeDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindNewsDynamicProviderFactory(factory: NewsDynamicProviderFactory): IDynamicDependenciesProviderFactory

    @Binds
    @IntoSet
    abstract fun bindWebContentDynamicProviderFactory(factory: WebContentDynamicProviderFactory): IDynamicDependenciesProviderFactory
}