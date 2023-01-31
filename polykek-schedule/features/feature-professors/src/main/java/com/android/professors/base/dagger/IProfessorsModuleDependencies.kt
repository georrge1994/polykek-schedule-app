package com.android.professors.base.dagger

import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.ITeacherToProfessorConvertor
import retrofit2.Retrofit

/**
 * Professors module dependencies.
 */
interface IProfessorsModuleDependencies : ICoreUiModuleDependencies {
    val professorsNavigationActions: IProfessorsNavigationActions
    val teacherToProfessorConvertor: ITeacherToProfessorConvertor
    val scheduleController: IScheduleController
    val savedItemsRoomRepository: ISavedItemsRoomRepository
    val retrofit: Retrofit
}