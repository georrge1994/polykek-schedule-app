package com.android.feature.schools.viewModels

import com.android.feature.schools.useCases.SchoolUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.collectPost
import com.android.test.support.androidTest.utils.getOrAwaitValue
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
    private val schoolUseCase: SchoolUseCase = mockk() {
        coEvery { getSchools() } returns emptyList()
    }
    private val schoolViewModel = SchoolViewModel(schoolUseCase)

    /**
     * Update schools.
     */
    @Test
    fun updateSchools() = runBlockingUnit {
        schoolViewModel.isLoading.collectPost {
            schoolViewModel.updateSchools()
            schoolViewModel.schools.getOrAwaitValue(emptyList())
            coVerify(exactly = 1) { schoolUseCase.getSchools() }
        }.apply {
            assertEquals(2, this.size)
            assertEquals(true, first())
            assertEquals(false, last())
        }
    }
}