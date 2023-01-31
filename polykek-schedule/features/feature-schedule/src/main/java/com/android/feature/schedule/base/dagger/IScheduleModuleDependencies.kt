package com.android.feature.schedule.base.dagger

import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.schedule.controller.api.IProfessorScheduleUseCase
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleDateUseCase

/**
 * Schedule module dependencies.
 */
interface IScheduleModuleDependencies : ICoreUiModuleDependencies {
    val scheduleNavigationActions: IScheduleNavigationActions
    val scheduleController: IScheduleController
    val scheduleDateUseCase: IScheduleDateUseCase
    val professorScheduleUseCase: IProfessorScheduleUseCase
}