package com.android.feature.schedule.base.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Schedule screen component holder.
 */
object ScheduleComponentHolder : ComponentHolder<IScheduleModuleApi, IScheduleModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IScheduleModuleApi,
            IScheduleModuleDependencies, ScheduleComponent> { ScheduleComponent.create(it) }

    override fun getApi(): IScheduleModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [ScheduleComponent]
     */
    internal fun getComponent(): ScheduleComponent = getApi() as ScheduleComponent
}