package com.android.feature.schools.useCases

import com.android.common.models.api.Resource
import com.android.feature.schools.api.SchoolApiRepository
import com.android.feature.schools.api.SchoolResponse
import com.android.feature.schools.api.SchoolsResponse
import com.android.feature.schools.models.School
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.unitTest.base.BaseUnitTestForSubscriptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * School use case test for [SchoolUseCase].
 *
 * @constructor Create empty constructor for school use case test
 */
class SchoolUseCaseTest : BaseUnitTestForSubscriptions() {
    private val schoolListRepository: SchoolApiRepository = mockk {
        coEvery { getSchools() } returns Resource.Success(
            data = SchoolsResponse(
                faculties = listOf(
                    SchoolResponse(id = "1L", name = "name1", abbr = "abbr1"),
                    SchoolResponse(id = "2L", name = "name2", abbr = "abbr2")
                )
            )
        )
    }
    private val backgroundMessageBus = MutableSharedFlow<String>(replay = 1)
    private val schoolUseCase = SchoolUseCase(schoolListRepository, backgroundMessageBus)

    /**
     * Get schools.
     */
    @Test
    fun getSchoolsSuccess() = runTest {
        assertEquals(
            listOf(
                School(id = "1L", name = "name1", abbr = "abbr1"),
                School(id = "2L", name = "name2", abbr = "abbr2")
            ),
            schoolUseCase.getSchools()
        )
        coVerify(exactly = 1) { schoolListRepository.getSchools() }
    }

    /**
     * Get schools failed.
     */
    @Test
    fun getSchoolsFailed() = runTest {
        coEvery { schoolListRepository.getSchools() } returns Resource.Error(errorMessage = "failed")
        assertEquals(null, schoolUseCase.getSchools())
        backgroundMessageBus.subscribeAndCompareFirstValue("failed").joinWithTimeout()
        coVerify(exactly = 1) { schoolListRepository.getSchools() }
    }
}