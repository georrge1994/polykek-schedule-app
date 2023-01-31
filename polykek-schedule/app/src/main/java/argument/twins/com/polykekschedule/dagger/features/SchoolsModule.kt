package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.core.CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER
import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.retrofit.impl.dagger.ICoreRetrofitDependencies
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.core.ui.models.ScheduleMode
import com.android.feature.groups.dagger.GroupsComponentHolder
import com.android.feature.groups.dagger.IGroupsModuleDependencies
import com.android.feature.schools.dagger.ISchoolsModuleDependencies
import com.android.feature.schools.dagger.ISchoolsNavigationActions
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

internal const val SCHOOLS_DYNAMIC_DEPENDENCIES_PROVIDER = "SCHOOLS_DYNAMIC_DEPENDENCIES_PROVIDER"

@Module
class SchoolsModule {
    private class SchoolsModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModule: ICoreRetrofitModuleApi,
        override val block: (IBaseDependencyHolder<ISchoolsModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi) -> ISchoolsModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, ICoreRetrofitModuleApi, ISchoolsModuleDependencies>(coreUiModuleApi, coreRetrofitModule)

    @Provides
    @Singleton
    @Named(SCHOOLS_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForSchools(
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>,
        @Named(CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreRetrofit: DynamicProvider<ICoreRetrofitDependencies>,
        @Named(GROUPS_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForGroups: DynamicProvider<IGroupsModuleDependencies>
    ) = DynamicProvider {
        SchoolsModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi),
            CoreRetrofitComponentHolder.initAndGet(dynamicDependencyProviderForCoreRetrofit)
        ) { dependencyHolder, coreUiModuleApi, coreRetrofitModuleApi ->
            object : ISchoolsModuleDependencies {
                override val schoolsNavigationActions: ISchoolsNavigationActions
                    get() = getSchoolsNavigationActions(dynamicDependencyProviderForGroups)
                override val retrofit: Retrofit
                    get() = coreRetrofitModuleApi.retrofit
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }

    /**
     * Get schools navigation actions. Create groups component only if user clicks to it.
     *
     * @param dynamicDependencyProviderForGroups Dynamic dependency provider for groups
     * @return [ISchoolsNavigationActions]
     */
    private fun getSchoolsNavigationActions(
        dynamicDependencyProviderForGroups: DynamicProvider<IGroupsModuleDependencies>
    ) = object : ISchoolsNavigationActions {
        override fun getGroupsScreen(scheduleMode: ScheduleMode, schoolId: String, abbr: String): Fragment =
            GroupsComponentHolder.initAndGet(dynamicDependencyProviderForGroups).getGroupsFragment(scheduleMode, schoolId, abbr)
    }
}