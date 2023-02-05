package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.models.ScheduleMode
import com.android.feature.groups.dagger.GroupsComponentHolder
import com.android.feature.schools.dagger.ISchoolsModuleDependencies
import com.android.feature.schools.dagger.ISchoolsNavigationActions
import com.android.feature.schools.dagger.SchoolsComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Schools dynamic provider factory.
 *
 * @constructor Create empty constructor for schools dynamic provider factory
 */
class SchoolsDynamicProviderFactory @Inject constructor() :
    DynamicDependenciesProviderFactory<SchoolsComponentHolder, ISchoolsModuleDependencies>(SchoolsComponentHolder) {
    private class SchoolsModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModule: ICoreRetrofitModuleApi,
        override val block: (IBaseDependencyHolder<ISchoolsModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi) -> ISchoolsModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, ICoreRetrofitModuleApi, ISchoolsModuleDependencies>(coreUiModuleApi, coreRetrofitModule)

    /**
     * Get schools navigation actions. Create groups component only if user clicks to it.
     */
    private val schoolsNavigationActions: ISchoolsNavigationActions = object : ISchoolsNavigationActions {
        override fun getGroupsScreen(scheduleMode: ScheduleMode, schoolId: String, abbr: String): Fragment =
            GroupsComponentHolder.getApi().getGroupsFragment(scheduleMode, schoolId, abbr)
    }

    override fun getDynamicProvider(): DynamicProvider<ISchoolsModuleDependencies> = DynamicProvider {
        SchoolsModuleDependenciesHolder(
            CoreUiComponentHolder.getApi(),
            CoreRetrofitComponentHolder.getApi()
        ) { dependencyHolder, coreUiModuleApi, coreRetrofitModuleApi ->
            object : ISchoolsModuleDependencies {
                override val schoolsNavigationActions: ISchoolsNavigationActions
                    get() = this@SchoolsDynamicProviderFactory.schoolsNavigationActions
                override val retrofit: Retrofit
                    get() = coreRetrofitModuleApi.retrofit
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}