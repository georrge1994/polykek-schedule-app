package com.android.professors.base.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.professors.list.fragments.ProfessorsFragment
import com.android.professors.search.fragments.ProfessorSearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IProfessorsModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        ProfessorsModuleInjector::class
    ]
)
internal abstract class ProfessorsComponent : IModuleComponent, IProfessorsModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: ProfessorsFragment)
    internal abstract fun inject(fragment: ProfessorSearchFragment)

    override fun getProfessorsFragment(): Fragment = ProfessorsFragment()

    override fun getProfessorSearchFragment(): Fragment = ProfessorSearchFragment()

    companion object {
        /**
         * Create [ProfessorsComponent].
         *
         * @param professorsModuleDependencies Professors module dependencies
         * @return [ProfessorsComponent]
         */
        internal fun create(professorsModuleDependencies: IProfessorsModuleDependencies): ProfessorsComponent =
            DaggerProfessorsComponent.builder()
                .iProfessorsModuleDependencies(professorsModuleDependencies)
                .build()
    }
}