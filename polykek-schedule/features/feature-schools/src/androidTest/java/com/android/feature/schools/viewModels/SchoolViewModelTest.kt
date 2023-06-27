package com.android.feature.schools.viewModels

import com.android.core.ui.models.ScheduleMode
import com.android.feature.schools.models.School
import com.android.feature.schools.mvi.SchoolIntent
import com.android.feature.schools.mvi.SchoolState
import com.android.feature.schools.useCases.SchoolUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * School view model test for [SchoolViewModel].
 *
 * @constructor Create empty constructor for school view model test
 */
class SchoolViewModelTest : BaseViewModelUnitTest() {
    private val schoolsMockk = listOf(
        School(id = "1", name = "School 1", abbr = "S1")
    )
    private val schoolUseCase: SchoolUseCase = mockk {
        coEvery { getSchools() } returns schoolsMockk
    }
    private val schoolViewModel = SchoolViewModel(schoolUseCase)

    /**
     * Update schools.
     */
    @Test
    fun updateSchools() = runBlockingUnit {
        schoolViewModel.state.collectPost {
            schoolViewModel.dispatchIntentAsync(SchoolIntent.InitContent(ScheduleMode.WELCOME)).joinWithTimeout()
            coVerify(exactly = 1) { schoolUseCase.getSchools() }
        }.apply {
            assertEquals(3, this.size)
            this[0]?.checkState()                                                       // Default state.
            this[1]?.checkState(isLoading = true, scheduleMode = ScheduleMode.WELCOME)  // Init content state.
            this[2]?.checkState(                                                        // Update schools state.
                isLoading = false,
                schools = schoolsMockk,
                scheduleMode = ScheduleMode.WELCOME
            )
        }
    }

    /**
     * Check state.
     *
     * @receiver [SchoolState]
     * @param isLoading Is loading
     * @param schools Schools
     * @param scheduleMode Schedule mode
     */
    private fun SchoolState?.checkState(
        isLoading: Boolean = false,
        schools: List<School> = emptyList(),
        scheduleMode: ScheduleMode = ScheduleMode.SEARCH
    ) {
        this ?: throw Exception("SchoolState is null")
        assertEquals(isLoading, this.isLoading)
        assertEquals(schools, this.schools)
        assertEquals(scheduleMode, this.scheduleMode)
    }
}