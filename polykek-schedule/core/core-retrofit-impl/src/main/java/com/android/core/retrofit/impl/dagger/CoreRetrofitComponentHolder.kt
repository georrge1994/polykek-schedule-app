package com.android.core.retrofit.impl.dagger

import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Core retrofit component holder.
 */
object CoreRetrofitComponentHolder : ComponentHolder<ICoreRetrofitModuleApi, ICoreRetrofitDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<ICoreRetrofitModuleApi,
            ICoreRetrofitDependencies, CoreRetrofitComponent> { CoreRetrofitComponent.create(it) }

    override fun getApi(): ICoreRetrofitModuleApi = componentHolderDelegate.getApi(dependenciesProvider)
}