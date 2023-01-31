package com.android.feature.faq.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * FAQ screen component holder.
 */
object FaqComponentHolder : ComponentHolder<IFaqModuleApi, IFaqModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IFaqModuleApi,
            IFaqModuleDependencies, FaqComponent> { FaqComponent.create(it) }

    override fun getApi(): IFaqModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [FaqComponent]
     */
    internal fun getComponent(): FaqComponent = getApi() as FaqComponent
}