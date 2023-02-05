package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.feature.schools.dagger.SchoolsComponentHolder
import com.android.feature.welcome.dagger.IWelcomeModuleDependencies
import com.android.feature.welcome.dagger.IWelcomeNavigationActions
import com.android.feature.welcome.dagger.WelcomeComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.professors.base.dagger.ProfessorsComponentHolder
import javax.inject.Inject

/**
 * Welcome dynamic provider factory.
 *
 * @constructor Create empty constructor for welcome dynamic provider factory
 */
class WelcomeDynamicProviderFactory @Inject constructor() :
    DynamicDependenciesProviderFactory<WelcomeComponentHolder, IWelcomeModuleDependencies>(WelcomeComponentHolder) {
    private class WelcomeModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<IWelcomeModuleDependencies>, ICoreUiModuleApi) -> IWelcomeModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, IWelcomeModuleDependencies>(coreUiModuleApi)

    /**
     * Get welcome navigation actions. Create instances of components only by user click.
     */
    private val welcomeNavigationActions: IWelcomeNavigationActions = object : IWelcomeNavigationActions {
        override fun getSchools(): Fragment = SchoolsComponentHolder.getApi().getSchoolsFragment()

        override fun getProfessorSearch(): Fragment = ProfessorsComponentHolder.getApi().getProfessorSearchFragment()
    }

    override fun getDynamicProvider(): DynamicProvider<IWelcomeModuleDependencies> = DynamicProvider {
        WelcomeModuleDependenciesHolder(CoreUiComponentHolder.getApi()) { dependencyHolder, coreUiModuleApi ->
            object : IWelcomeModuleDependencies {
                override val welcomeNavigationActions: IWelcomeNavigationActions
                    get() = this@WelcomeDynamicProviderFactory.welcomeNavigationActions
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}