package com.android.feature.welcome.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.feature.welcome.fragments.ChooseRoleFragment
import com.android.feature.welcome.fragments.ShootFragment
import com.android.feature.welcome.fragments.WelcomeFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IWelcomeModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        WelcomeModuleInjector::class
    ],
)
internal abstract class WelcomeComponent : IModuleComponent, IWelcomeModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: ShootFragment)
    internal abstract fun inject(fragment: WelcomeFragment)
    internal abstract fun inject(fragment: ChooseRoleFragment)

    override fun getWelcomeFragment(): Fragment = WelcomeFragment()

    companion object {
        /**
         * Create [WelcomeComponent].
         *
         * @param welcomeModuleDependencies Welcome module dependencies
         * @return [WelcomeComponent]
         */
        internal fun create(welcomeModuleDependencies: IWelcomeModuleDependencies): WelcomeComponent =
            DaggerWelcomeComponent.builder()
                .iWelcomeModuleDependencies(welcomeModuleDependencies)
                .build()
    }
}