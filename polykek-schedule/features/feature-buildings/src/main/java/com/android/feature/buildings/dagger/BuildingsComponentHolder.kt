package com.android.feature.buildings.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Buildings screen component holder.
 */
object BuildingsComponentHolder : ComponentHolder<IBuildingsModuleApi, IBuildingsModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IBuildingsModuleApi,
            IBuildingsModuleDependencies, BuildingsComponent> { BuildingsComponent.create(it) }

    override fun getApi(): IBuildingsModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [BuildingsComponent]
     */
    internal fun getComponent(): BuildingsComponent = getApi() as BuildingsComponent
}