package com.android.professors.search.viewModels

import com.android.common.models.professors.Professor
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.models.ScheduleMode
import com.android.feature.professors.R
import com.android.professors.search.mvi.ProfessorsSearchAction
import com.android.professors.search.mvi.ProfessorsSearchIntent
import com.android.professors.search.mvi.ProfessorsSearchState
import com.android.professors.search.useCases.ProfessorsSearchUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Professor search view model test.
 *
 * @constructor Create empty constructor for professor search view model test
 */
class ProfessorSearchViewModelTest : BaseViewModelUnitTest() {
    private val professors = listOf<Professor>(mockk(), mockk())
    private val backgroundMessageBus = MutableSharedFlow<String>(replay = 1)
    private val professorsSearchUseCase: ProfessorsSearchUseCase = mockk {
        coEvery { getProfessors(any()) } returns professors
    }
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk {
        coEvery { saveItemAndSelectIt(any()) } returns Unit
    }

    private val professorSearchViewModel = ProfessorSearchViewModel(
        application = application,
        backgroundMessageBus = backgroundMessageBus,
        professorsSearchUseCase = professorsSearchUseCase,
        savedItemsRoomRepository = savedItemsRoomRepository
    )

    /**
     * Init content.
     */
    @Test
    fun initContent() = runBlockingUnit {
        professorSearchViewModel.dispatchIntentAsync(
            ProfessorsSearchIntent.InitContent(ScheduleMode.NEW_ITEM)
        ).joinWithTimeout()
        professorSearchViewModel.state.getOrAwaitValue().apply {
            assertEquals(ScheduleMode.NEW_ITEM, scheduleMode)
            assertEquals(emptyList<Professor>(), professors)
        }
    }

    /**
     * Init content 2.
     */
    @Test
    fun initContent2() = runBlockingUnit {
        professorSearchViewModel.dispatchIntentAsync(
            ProfessorsSearchIntent.InitContent(ScheduleMode.SEARCH)
        ).joinWithTimeout()
        professorSearchViewModel.state.getOrAwaitValue().apply {
            assertEquals(ScheduleMode.SEARCH, scheduleMode)
        }
    }

    /**
     * Search professors by keyword.
     */
    @Test
    fun searchProfessorsByKeyword_tooShortKeyword() = runBlockingUnit {
        val keyWord = "ab"
        professorSearchViewModel.dispatchIntentAsync(
            ProfessorsSearchIntent.SearchProfessorsByKeyword(keyWord)
        ).joinWithTimeout()
        backgroundMessageBus.subscribeAndCompareFirstValue(TEST_STRING).joinWithTimeout()
    }

    /**
     * Search professors by keyword 2.
     */
    @Test
    fun searchProfessorsByKeyword_normalKeyword() = runBlockingUnit {
        val keyWord = "Petr"
        professorSearchViewModel.state.collectPost {
            professorSearchViewModel.dispatchIntentAsync(
                ProfessorsSearchIntent.SearchProfessorsByKeyword(keyWord)
            ).joinWithTimeout()
            coVerify(exactly = 1) { professorsSearchUseCase.getProfessors(keyWord) }
        }.apply {
            assertEquals(3, this.size)
            assertEquals(ProfessorsSearchState.Default, this[0])
            assertEquals(ProfessorsSearchState.Default.copyState(isLoading = true), this[1])
            assertEquals(
                ProfessorsSearchState.Default.copyState(
                    isLoading = false,
                    professors = professors,
                    messageId = R.string.professors_search_fragment_no_professors
                ),
                this[2]
            )
        }
    }

    /**
     * Select item and show the next screen.
     */
    @Test
    fun selectProfessor() = runBlockingUnit {
        val professor = mockk<Professor> {
            coEvery { toSavedItem() } returns mockk()
        }
        val actionJob = professorSearchViewModel.action.subscribeAndCompareFirstValue(
            ProfessorsSearchAction.ShowNextScreen(ScheduleMode.WELCOME, professor)
        )
        professorSearchViewModel.dispatchIntentAsync(
            ProfessorsSearchIntent.InitContent(ScheduleMode.WELCOME)
        ).joinWithTimeout()
        professorSearchViewModel.dispatchIntentAsync(
            ProfessorsSearchIntent.SaveAndShowNextScreen(professor)
        ).joinWithTimeout()
        coVerify(exactly = 1) { savedItemsRoomRepository.saveItemAndSelectIt(any()) }
        actionJob.joinWithTimeout()
    }
}