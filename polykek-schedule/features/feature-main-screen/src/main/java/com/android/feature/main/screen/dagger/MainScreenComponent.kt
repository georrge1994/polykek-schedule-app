package com.android.feature.main.screen.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.feature.main.screen.menu.fragments.BottomSheetFragment
import com.android.feature.main.screen.menu.fragments.MainFragment
import com.android.feature.main.screen.menu.fragments.TabContainerFragment
import com.android.feature.main.screen.saved.fragments.SavedItemsFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IMainScreenModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        MainScreenModuleInjector::class
    ]
)
internal abstract class MainScreenComponent : IModuleComponent, IMainScreenModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: MainFragment)
    internal abstract fun inject(fragment: BottomSheetFragment)
    internal abstract fun inject(fragment: TabContainerFragment)
    internal abstract fun inject(fragment: SavedItemsFragment)

    override fun getMainFragment(): Fragment = MainFragment()

    companion object {
        /**
         * Create [MainScreenComponent].
         *
         * @param mainScreenModuleDependencies Main screen module dependencies
         * @return [MainScreenComponent]
         */
        internal fun create(mainScreenModuleDependencies: IMainScreenModuleDependencies): MainScreenComponent =
            DaggerMainScreenComponent.builder()
                .iMainScreenModuleDependencies(mainScreenModuleDependencies)
                .build()
    }
}