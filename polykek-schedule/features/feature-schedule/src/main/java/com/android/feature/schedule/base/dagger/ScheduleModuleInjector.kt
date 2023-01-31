package com.android.feature.schedule.base.dagger

import com.android.feature.schedule.professor.dagger.ProfessorScheduleModuleInjector
import com.android.feature.schedule.student.dagger.StudentScheduleModule
import dagger.Module

@Module(
    includes = [
        StudentScheduleModule::class,
        ProfessorScheduleModuleInjector::class
    ]
)
abstract class ScheduleModuleInjector