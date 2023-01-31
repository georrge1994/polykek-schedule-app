package com.android.professors.list.viewModels

import androidx.lifecycle.MutableLiveData
import com.android.common.models.professors.Professor
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.professors.list.useCases.ProfessorsUseCase
import com.android.schedule.controller.api.IScheduleController
import com.android.shared.code.utils.liveData.EventLiveData
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Professors view model provides professors, which hold lessons on the current week.
 *
 * @property scheduleController Schedule controller allows to synchronize UI changes between all tabs
 * @property professorsUseCase Professors use case provides fetching professors from week
 * @constructor Create [ProfessorsViewModel]
 */
internal class ProfessorsViewModel @Inject constructor(
    private val scheduleController: IScheduleController,
    private val professorsUseCase: ProfessorsUseCase
) : BaseSubscriptionViewModel() {
    val professors = MutableLiveData<List<Professor>>()
    val isListEmpty = EventLiveData<Boolean>()

    override suspend fun subscribe() {
        super.subscribe()
        subscribeToWeekFlow()
    }

    /**
     * Subscribe to week flow.
     */
    private fun subscribeToWeekFlow() = scheduleController.weekFlow
        .onEach { week ->
            week?.let {
                val professors = professorsUseCase.getProfessors(week)
                this@ProfessorsViewModel.professors.postValue(professors)
                isListEmpty.postValue(professors.isEmpty())
            } ?: isListEmpty.postValue(true)
        }.cancelableLaunchInBackground()
}