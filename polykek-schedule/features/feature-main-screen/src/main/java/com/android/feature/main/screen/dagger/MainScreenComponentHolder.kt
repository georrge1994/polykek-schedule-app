package com.android.feature.main.screen.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Main screen component holder.
 */
object MainScreenComponentHolder : ComponentHolder<IMainScreenModuleApi, IMainScreenModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IMainScreenModuleApi,
            IMainScreenModuleDependencies, MainScreenComponent> { MainScreenComponent.create(it) }

    override fun getApi(): IMainScreenModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [MainScreenComponent]
     */
    internal fun getComponent(): MainScreenComponent = getApi() as MainScreenComponent
}