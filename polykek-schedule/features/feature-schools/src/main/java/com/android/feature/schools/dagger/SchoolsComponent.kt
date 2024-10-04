package com.android.feature.schools.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.feature.schools.fragments.SchoolsFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [ISchoolsModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        SchoolsModuleInjector::class
    ]
)
internal abstract class SchoolsComponent : IModuleComponent, ISchoolsModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: SchoolsFragment)

    override fun getSchoolsFragment(): Fragment = SchoolsFragment()

    internal companion object {
        /**
         * Create [SchoolsComponent].
         *
         * @param schoolsModuleDependencies Schools module dependencies
         * @return [SchoolsComponent]
         */
        internal fun create(schoolsModuleDependencies: ISchoolsModuleDependencies): SchoolsComponent =
            DaggerSchoolsComponent.builder()
                .iSchoolsModuleDependencies(schoolsModuleDependencies)
                .build()
    }
}