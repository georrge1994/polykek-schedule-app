package com.android.feature.schools.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Schools screen component holder.
 */
object SchoolsComponentHolder : ComponentHolder<ISchoolsModuleApi, ISchoolsModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<ISchoolsModuleApi,
            ISchoolsModuleDependencies, SchoolsComponent> { SchoolsComponent.create(it) }

    override fun getApi(): ISchoolsModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [SchoolsComponent]
     */
    internal fun getComponent(): SchoolsComponent = getApi() as SchoolsComponent
}