package com.android.feature.map.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Map screen component holder.
 */
object MapComponentHolder : ComponentHolder<IMapModuleApi, IMapModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IMapModuleApi,
            IMapModuleDependencies, MapComponent> { MapComponent.create(it) }

    override fun getApi(): IMapModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [MapComponent]
     */
    internal fun getComponent(): MapComponent = getApi() as MapComponent
}