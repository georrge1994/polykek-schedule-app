package com.example.feature.web.content.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Web content screen component holder.
 */
object WebContentComponentHolder : ComponentHolder<IWebContentModuleApi, IWebContentModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IWebContentModuleApi,
            IWebContentModuleDependencies, WebContentComponent> { WebContentComponent.create(it) }

    override fun getApi(): IWebContentModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [WebContentComponent]
     */
    internal fun getComponent(): WebContentComponent = getApi() as WebContentComponent
}