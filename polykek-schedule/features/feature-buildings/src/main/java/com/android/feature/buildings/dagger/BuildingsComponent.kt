package com.android.feature.buildings.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.feature.buildings.fragments.BuildingsFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IBuildingsModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        BuildingsModuleInjector::class
    ]
)
internal abstract class BuildingsComponent : IModuleComponent, IBuildingsModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: BuildingsFragment)

    override fun getBuildingsFragment(): Fragment = BuildingsFragment()

    internal companion object {
        /**
         * Create [BuildingsComponent].
         *
         * @param buildingsModuleDependencies Buildings module dependencies
         * @return [BuildingsComponent]
         */
        internal fun create(buildingsModuleDependencies: IBuildingsModuleDependencies): BuildingsComponent =
            DaggerBuildingsComponent.builder()
                .iBuildingsModuleDependencies(buildingsModuleDependencies)
                .build()
    }
}