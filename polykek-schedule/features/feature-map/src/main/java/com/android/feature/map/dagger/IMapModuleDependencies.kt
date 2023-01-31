package com.android.feature.map.dagger

import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.schedule.controller.api.IScheduleController

/**
 * Map module dependencies.
 */
interface IMapModuleDependencies : ICoreUiModuleDependencies {
    val mapNavigationActions: IMapNavigationActions
    val scheduleController: IScheduleController
}