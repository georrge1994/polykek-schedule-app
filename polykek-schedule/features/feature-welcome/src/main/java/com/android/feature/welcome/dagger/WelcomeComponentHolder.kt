package com.android.feature.welcome.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Welcome screen component holder.
 */
object WelcomeComponentHolder : ComponentHolder<IWelcomeModuleApi, IWelcomeModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IWelcomeModuleApi,
            IWelcomeModuleDependencies, WelcomeComponent> { WelcomeComponent.create(it) }

    override fun getApi(): IWelcomeModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [WelcomeComponent]
     */
    internal fun getComponent(): WelcomeComponent = getApi() as WelcomeComponent
}