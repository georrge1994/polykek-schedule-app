package com.android.professors.search.useCases

import com.android.common.models.api.Resource
import com.android.professors.search.api.ProfessorsApiRepository
import com.android.professors.search.api.TeachersResponse
import com.android.schedule.controller.api.ITeacherToProfessorConvertor
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.unitTest.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Professors search use case test for [ProfessorsSearchUseCase].
 *
 * @constructor Create empty constructor for professors search use case test
 */
class ProfessorsSearchUseCaseTest : BaseUnitTest() {
    private val professorScheduleRepository: ProfessorsApiRepository = mockk {
        coEvery { getTeachersByKey(any()) } returns Resource.Success(
            data = TeachersResponse(
                teachers = listOf(mockk(), mockk())
            )
        )
    }
    private val teacherToProfessorConvertor: ITeacherToProfessorConvertor = mockk {
        coEvery { convertTeacherToProfessor(any()) } returns mockk {
            coEvery { fullName } returns TEST_STRING
        }
    }
    private val backgroundMessageBus = MutableSharedFlow<String>()

    private val professorsSearchUseCase = ProfessorsSearchUseCase(
        professorScheduleRepository = professorScheduleRepository,
        teacherToProfessorConvertor = teacherToProfessorConvertor,
        backgroundMessageBus = backgroundMessageBus
    )

    /**
     * Get professors success.
     */
    @Test
    fun getProfessorsSuccess() = runTest {
        val result = professorsSearchUseCase.getProfessors("Пе")
        coVerify(exactly = 1) { professorScheduleRepository.getTeachersByKey("Пе") }
        coVerify(exactly = 2) { teacherToProfessorConvertor.convertTeacherToProfessor(any()) }
        assertEquals(2, result?.size)
    }

    /**
     * Get professors error.
     */
    @Test
    fun getProfessorsError() = runTest {
        coEvery { professorScheduleRepository.getTeachersByKey(any()) } returns Resource.Error("oops")
        val result = professorsSearchUseCase.getProfessors("Пе")
        coVerify(exactly = 1) { professorScheduleRepository.getTeachersByKey("Пе") }
        coVerify(exactly = 0) { teacherToProfessorConvertor.convertTeacherToProfessor(any()) }
        assertEquals(null, result)
    }
}