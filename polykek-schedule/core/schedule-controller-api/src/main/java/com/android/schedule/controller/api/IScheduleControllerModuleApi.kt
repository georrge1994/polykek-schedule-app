package com.android.schedule.controller.api

import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Schedule controller module api.
 */
interface IScheduleControllerModuleApi : IModuleApi {
    val scheduleDateUseCase: IScheduleDateUseCase
    val teacherToProfessorConvertor: ITeacherToProfessorConvertor
    val scheduleController: IScheduleController
    val professorScheduleUseCase: IProfessorScheduleUseCase
}