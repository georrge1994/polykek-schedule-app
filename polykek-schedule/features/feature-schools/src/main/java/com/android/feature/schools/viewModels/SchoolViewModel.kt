package com.android.feature.schools.viewModels

import androidx.lifecycle.MutableLiveData
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.feature.schools.models.School
import com.android.feature.schools.useCases.SchoolUseCase
import javax.inject.Inject

/**
 * School view model provides general logic for school-screen.
 *
 * @property schoolUseCase This use case is an interactor before school repository
 * @constructor Create [SchoolViewModel]
 */
internal class SchoolViewModel @Inject constructor(private val schoolUseCase: SchoolUseCase) : BaseSubscriptionViewModel() {
    val schools = MutableLiveData<List<School>?>()
    var scheduleMode = ScheduleMode.SEARCH

    /**
     * Update schools.
     */
    internal fun updateSchools() = executeWithLoadingAnimation {
        schoolUseCase.getSchools().apply {
            schools.postValue(this)
        }
    }
}