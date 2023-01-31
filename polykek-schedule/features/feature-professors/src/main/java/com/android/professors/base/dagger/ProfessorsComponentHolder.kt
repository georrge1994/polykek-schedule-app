package com.android.professors.base.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Professors screen component holder.
 */
object ProfessorsComponentHolder : ComponentHolder<IProfessorsModuleApi, IProfessorsModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IProfessorsModuleApi,
            IProfessorsModuleDependencies, ProfessorsComponent> { ProfessorsComponent.create(it) }

    override fun getApi(): IProfessorsModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [ProfessorsComponent]
     */
    internal fun getComponent(): ProfessorsComponent = getApi() as ProfessorsComponent
}