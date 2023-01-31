package com.android.professors.search.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.common.models.professors.Professor
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.feature.professors.R
import com.android.professors.search.useCases.ProfessorsSearchUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Named

private const val MIN_COUNT_OF_SYMBOLS = 3

/**
 * Provides logic for searching professors by name + professor list.
 *
 * @property application Application object to get context
 * @property backgroundMessageBus Common background message bus with snack-bar output
 * @property professorsSearchUseCase Professors search use case
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [ProfessorSearchViewModel]
 */
internal class ProfessorSearchViewModel @Inject constructor(
    private val application: Application,
    @Named(BACKGROUND_MESSAGE_BUS) private val backgroundMessageBus: MutableSharedFlow<String>,
    private val professorsSearchUseCase: ProfessorsSearchUseCase,
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : BaseSubscriptionViewModel() {
    var scheduleMode = ScheduleMode.SEARCH

    val professors = MutableLiveData<List<Professor>>()
    val listIsEmpty = Transformations.map(professors) {
        it.isNullOrEmpty()
    }
    val messageId = Transformations.map(professors) {
        if (it.isNullOrEmpty())
            R.string.professors_search_fragment_no_professors
        else
            R.string.professors_search_fragment_manual_text
    }

    override suspend fun subscribe() {
        super.subscribe()
        // Clear after first launch (actual only if user chosen the professor role).
        if (scheduleMode == ScheduleMode.NEW_ITEM)
            professors.postValue(Collections.emptyList())
    }

    /**
     * Search professors by keyword.
     *
     * @param keyword Keyword
     */
    internal fun searchProfessorsByKeyword(keyword: String) = executeWithLoadingAnimation {
        if (keyword.length < MIN_COUNT_OF_SYMBOLS) {
            backgroundMessageBus.emit(application.getString(R.string.professors_search_wrong_length_of_name))
        } else {
            professorsSearchUseCase.getProfessors(keyword).let { professors ->
                this.professors.postValue(professors)
            }
        }
    }

    /**
     * Save selected item.
     *
     * @param professor Professor
     */
    internal fun saveSelectedItem(professor: Professor) = launchInBackground {
        savedItemsRoomRepository.saveItemAndSelectIt(professor.toSavedItem())
    }
}