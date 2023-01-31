package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.feature.schools.dagger.ISchoolsModuleDependencies
import com.android.feature.schools.dagger.SchoolsComponentHolder
import com.android.feature.welcome.dagger.IWelcomeModuleDependencies
import com.android.feature.welcome.dagger.IWelcomeNavigationActions
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.professors.base.dagger.IProfessorsModuleDependencies
import com.android.professors.base.dagger.ProfessorsComponentHolder
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

internal const val WELCOME_DYNAMIC_DEPENDENCIES_PROVIDER = "WELCOME_DYNAMIC_DEPENDENCIES_PROVIDER"

@Module
class WelcomeModule {
    private class WelcomeModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<IWelcomeModuleDependencies>, ICoreUiModuleApi) -> IWelcomeModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, IWelcomeModuleDependencies>(coreUiModuleApi)

    @Provides
    @Singleton
    @Named(WELCOME_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForWelcome(
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>,
        @Named(SCHOOLS_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForSchools: DynamicProvider<ISchoolsModuleDependencies>,
        @Named(PROFESSORS_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForProfessors: DynamicProvider<IProfessorsModuleDependencies>,
    ) = DynamicProvider {
        WelcomeModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi)
        ) { dependencyHolder, coreUiModuleApi ->
            object : IWelcomeModuleDependencies {
                override val welcomeNavigationActions: IWelcomeNavigationActions
                    get() = getWelcomeNavigationActions(dynamicDependencyProviderForSchools, dynamicDependencyProviderForProfessors)
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }

    /**
     * Get welcome navigation actions. Create instances of components only by user click.
     *
     * @param dynamicDependencyProviderForSchools Dynamic dependency provider for schools
     * @param dynamicDependencyProviderForProfessors Dynamic dependency provider for professors
     * @return [IWelcomeNavigationActions]
     */
    private fun getWelcomeNavigationActions(
        dynamicDependencyProviderForSchools: DynamicProvider<ISchoolsModuleDependencies>,
        dynamicDependencyProviderForProfessors: DynamicProvider<IProfessorsModuleDependencies>
    ) = object : IWelcomeNavigationActions {
        override fun getSchools(): Fragment = SchoolsComponentHolder.initAndGet(dynamicDependencyProviderForSchools).getSchoolsFragment()

        override fun getProfessorSearch(): Fragment =
            ProfessorsComponentHolder.initAndGet(dynamicDependencyProviderForProfessors).getProfessorSearchFragment()
    }
}