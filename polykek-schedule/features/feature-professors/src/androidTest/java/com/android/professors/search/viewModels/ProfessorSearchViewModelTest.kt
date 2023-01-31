package com.android.professors.search.viewModels

import com.android.common.models.professors.Professor
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.models.ScheduleMode
import com.android.feature.professors.R
import com.android.professors.search.useCases.ProfessorsSearchUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.collectPost
import com.android.test.support.androidTest.utils.getOrAwaitValue
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
     * Complex test 1.
     */
    @Test
    fun complexTest1() = runBlockingUnit {
        professorSearchViewModel.scheduleMode = ScheduleMode.NEW_ITEM
        professorSearchViewModel.asyncSubscribe().joinWithTimeout()
        professorSearchViewModel.professors.getOrAwaitValue(emptyList())
        professorSearchViewModel.listIsEmpty.getOrAwaitValue(true)
        professorSearchViewModel.messageId.getOrAwaitValue(R.string.professors_search_fragment_no_professors)
        professorSearchViewModel.unSubscribe()
    }

    /**
     * Complex test 2.
     */
    @Test
    fun complexTest2() = runBlockingUnit {
        professorSearchViewModel.scheduleMode = ScheduleMode.SEARCH
        professorSearchViewModel.asyncSubscribe().joinWithTimeout()
        professorSearchViewModel.isLoading.collectPost {
            professorSearchViewModel.searchProfessorsByKeyword("k").joinWithTimeout()
            backgroundMessageBus.subscribeAndCompareFirstValue(TEST_STRING).joinWithTimeout()
        }.apply {
            assertEquals(2, this.size)
            assertEquals(true, first())
            assertEquals(false, last())
        }
        professorSearchViewModel.unSubscribe()
    }

    /**
     * Complex test 3.
     */
    @Test
    fun complexTest3() = runBlockingUnit {
        professorSearchViewModel.scheduleMode = ScheduleMode.SEARCH
        professorSearchViewModel.asyncSubscribe().joinWithTimeout()
        professorSearchViewModel.isLoading.collectPost {
            professorSearchViewModel.searchProfessorsByKeyword("Петр").joinWithTimeout()
            coVerify(exactly = 1) { professorsSearchUseCase.getProfessors("Петр") }
            professorSearchViewModel.professors.getOrAwaitValue(professors)
            professorSearchViewModel.listIsEmpty.getOrAwaitValue(false)
            professorSearchViewModel.messageId.getOrAwaitValue(R.string.professors_search_fragment_manual_text)
        }.apply {
            assertEquals(2, this.size)
            assertEquals(true, first())
            assertEquals(false, last())
        }
        professorSearchViewModel.unSubscribe()
    }

    /**
     * Save selected item.
     */
    @Test
    fun saveSelectedItem() = runBlockingUnit {
        val professor = mockk<Professor> {
            coEvery { toSavedItem() } returns mockk()
        }
        professorSearchViewModel.saveSelectedItem(professor).joinWithTimeout()
        coVerify(exactly = 1) { savedItemsRoomRepository.saveItemAndSelectIt(any()) }
    }
}