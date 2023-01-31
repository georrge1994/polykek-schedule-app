package com.android.schedule.controller.impl.dagger

import com.android.schedule.controller.api.IProfessorScheduleUseCase
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.schedule.controller.api.ITeacherToProfessorConvertor
import com.android.schedule.controller.impl.ScheduleController
import com.android.schedule.controller.impl.useCases.ProfessorScheduleUseCase
import com.android.schedule.controller.impl.useCases.ScheduleDateUseCase
import com.android.schedule.controller.impl.useCases.ScheduleResponseConvertUseCase
import dagger.Binds
import dagger.Module

@Module(includes = [ScheduleApiServiceProvider::class])
internal abstract class ScheduleControllerModuleInjector {
    @Binds
    internal abstract fun bindScheduleController(scheduleController: ScheduleController): IScheduleController

    @Binds
    internal abstract fun bindScheduleDateUseCase(scheduleDateUseCase: ScheduleDateUseCase): IScheduleDateUseCase

    @Binds
    internal abstract fun bindProfessorScheduleUseCase(scheduleDateUseCase: ProfessorScheduleUseCase): IProfessorScheduleUseCase

    @Binds
    internal abstract fun bindTeacherToProfessorConvertor(
        scheduleResponseConvertUseCase: ScheduleResponseConvertUseCase
    ): ITeacherToProfessorConvertor
}