package com.android.schedule.controller.impl.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate
import com.android.schedule.controller.api.IScheduleControllerModuleApi

/**
 * ScheduleController screen component holder.
 */
object ScheduleControllerComponentHolder : ComponentHolder<IScheduleControllerModuleApi, IScheduleControllerModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IScheduleControllerModuleApi,
            IScheduleControllerModuleDependencies, ScheduleControllerComponent> { ScheduleControllerComponent.create(it) }

    override fun getApi(): IScheduleControllerModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [ScheduleControllerComponent]
     */
    internal fun getComponent(): ScheduleControllerComponent = getApi() as ScheduleControllerComponent
}