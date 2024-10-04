package com.android.feature.faq.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.feature.faq.fragments.FaqFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IFaqModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        FaqModuleInjector::class
    ]
)
internal abstract class FaqComponent : IModuleComponent, IFaqModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: FaqFragment)

    override fun getFaqFragment(): Fragment = FaqFragment()

    internal companion object {
        /**
         * Create [FaqComponent].
         *
         * @param faqModuleDependencies Faq module dependencies
         * @return [FaqComponent]
         */
        internal fun create(faqModuleDependencies: IFaqModuleDependencies): FaqComponent =
            DaggerFaqComponent.builder()
                .iFaqModuleDependencies(faqModuleDependencies)
                .build()
    }
}