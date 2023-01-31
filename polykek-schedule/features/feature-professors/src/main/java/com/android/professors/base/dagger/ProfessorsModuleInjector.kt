package com.android.professors.base.dagger

import com.android.professors.list.dagger.ProfessorsModule
import com.android.professors.search.dagger.ProfessorsSearchModule
import dagger.Module

@Module(
    includes = [
        ProfessorsSearchModule::class,
        ProfessorsModule::class,
        ProfessorsApiServiceProvider::class
    ]
)
internal abstract class ProfessorsModuleInjector