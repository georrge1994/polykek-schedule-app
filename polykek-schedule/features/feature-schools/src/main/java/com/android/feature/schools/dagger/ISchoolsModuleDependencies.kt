package com.android.feature.schools.dagger

import com.android.core.ui.dagger.ICoreUiModuleDependencies
import retrofit2.Retrofit

/**
 * School module dependencies.
 */
interface ISchoolsModuleDependencies : ICoreUiModuleDependencies {
    val schoolsNavigationActions: ISchoolsNavigationActions
    val retrofit: Retrofit
}