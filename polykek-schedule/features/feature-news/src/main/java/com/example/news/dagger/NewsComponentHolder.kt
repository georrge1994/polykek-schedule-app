package com.example.news.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * News screen component holder.
 */
object NewsComponentHolder : ComponentHolder<INewsModuleApi, INewsModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<INewsModuleApi,
            INewsModuleDependencies, NewsComponent> { NewsComponent.create(it) }

    override fun getApi(): INewsModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [NewsComponent]
     */
    internal fun getComponent(): NewsComponent = getApi() as NewsComponent
}