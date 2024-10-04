package com.example.feature.web.content.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.example.feature.web.content.fragments.WebContentFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IWebContentModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        WebContentModuleInjector::class,
    ]
)
internal abstract class WebContentComponent : IModuleComponent, IWebContentModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: WebContentFragment)

    override fun getWebContentFragment(title: String, url: String, scrollToElementClass: String?): Fragment =
        WebContentFragment.newInstance(title, url, scrollToElementClass)

    internal companion object {
        /**
         * Create [WebContentComponent].
         *
         * @param webContentModuleDependencies Web content module dependencies
         * @return [WebContentComponent]
         */
        internal fun create(webContentModuleDependencies: IWebContentModuleDependencies): WebContentComponent =
            DaggerWebContentComponent.builder()
                .iWebContentModuleDependencies(webContentModuleDependencies)
                .build()
    }
}