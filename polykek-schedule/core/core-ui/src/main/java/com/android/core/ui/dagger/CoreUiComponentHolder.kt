package com.android.core.ui.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Component holder for base UI core.
 */
object CoreUiComponentHolder : ComponentHolder<ICoreUiModuleApi, ICoreUiModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<ICoreUiModuleApi,
            ICoreUiModuleDependencies, CoreUiComponent> { CoreUiComponent.create(it) }

    override fun getApi(): ICoreUiModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [CoreUiComponent]
     */
    internal fun getComponent(): CoreUiComponent = getApi() as CoreUiComponent
}