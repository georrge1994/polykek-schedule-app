package com.android.feature.groups.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Groups screen component holder.
 */
object GroupsComponentHolder : ComponentHolder<IGroupsModuleApi, IGroupsModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IGroupsModuleApi,
            IGroupsModuleDependencies, GroupsComponent> { GroupsComponent.create(it) }

    override fun getApi(): IGroupsModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [GroupsComponent]
     */
    internal fun getComponent(): GroupsComponent = getApi() as GroupsComponent
}