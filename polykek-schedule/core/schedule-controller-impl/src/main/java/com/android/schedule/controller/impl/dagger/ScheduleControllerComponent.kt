package com.android.schedule.controller.impl.dagger

import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.schedule.controller.api.IScheduleControllerModuleApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IScheduleControllerModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        ScheduleControllerModuleInjector::class
    ]
)
internal abstract class ScheduleControllerComponent : IModuleComponent, IScheduleControllerModuleApi {
    companion object {
        /**
         * Create [ScheduleControllerComponent].
         *
         * @param scheduleControllerModuleDependencies Core database dependencies
         * @return [ScheduleControllerComponent]
         */
        internal fun create(scheduleControllerModuleDependencies: IScheduleControllerModuleDependencies): ScheduleControllerComponent =
            DaggerScheduleControllerComponent.builder()
                .iScheduleControllerModuleDependencies(scheduleControllerModuleDependencies)
                .build()
    }
}