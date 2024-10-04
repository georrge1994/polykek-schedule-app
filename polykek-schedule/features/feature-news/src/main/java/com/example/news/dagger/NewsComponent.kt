package com.example.news.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.example.news.fragments.NewsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [INewsModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        NewsModuleInjector::class
    ]
)
internal abstract class NewsComponent : IModuleComponent, INewsModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: NewsFragment)

    override fun getNewsFragment(): Fragment = NewsFragment()

    internal companion object {
        /**
         * Create [NewsComponent].
         *
         * @param newsModuleDependencies News module dependencies
         * @return [NewsComponent]
         */
        internal fun create(newsModuleDependencies: INewsModuleDependencies): NewsComponent =
            DaggerNewsComponent.builder()
                .iNewsModuleDependencies(newsModuleDependencies)
                .build()
    }
}