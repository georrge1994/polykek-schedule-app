package com.android.feature.map.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.feature.map.fragments.MapFragment
import com.android.feature.map.fragments.MapToolbarFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IMapModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        MapModuleInjector::class
    ]
)
internal abstract class MapComponent : IModuleComponent, IMapModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: MapFragment)
    internal abstract fun inject(fragment: MapToolbarFragment)

    override fun getMapFragment(): Fragment = MapFragment()

    internal companion object {
        /**
         * Create [MapComponent].
         *
         * @param mapModuleDependencies Map module dependencies
         * @return [MapComponent]
         */
        internal fun create(mapModuleDependencies: IMapModuleDependencies): MapComponent =
            DaggerMapComponent.builder()
                .iMapModuleDependencies(mapModuleDependencies)
                .build()
    }
}