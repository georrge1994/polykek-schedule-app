package com.android.professors.search.api

import com.android.common.models.api.Resource
import com.android.test.support.unitTest.base.BaseApiRepositoryTest
import com.android.test.support.unitTest.checkNegativeResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Professors api repository test for [ProfessorsApiRepository].
 *
 * @constructor Create empty constructor for professors api repository test
 */
class ProfessorsApiRepositoryTest : BaseApiRepositoryTest() {
    private val professorsApiRepository = ProfessorsApiRepository(getApi(ProfessorsApiService::class.java))

    /**
     * Get teachers by key 404.
     */
    @Test
    fun getTeachersByKey_404() = runTest {
        enqueueResponse(code = 404)
        professorsApiRepository.getTeachersByKey("Пе").checkNegativeResult()
    }

    /**
     * Get teachers by key 200.
     */
    @Test
    fun getTeachersByKey_200() = runTest {
        enqueueResponse(fileName = "teachers-200.json", code = 200)
        professorsApiRepository.getTeachersByKey("Пе").apply {
            assert(this is Resource.Success)
            assert(data?.teachers?.isNotEmpty()== true)
            this.data?.teachers?.first()?.apply {
                assertEquals(id, 4914)
                assertEquals(fullName, "Петров Сергей Михайлович")
                assertEquals(firstName, "Петров")
                assertEquals(middleName, "Сергей")
                assertEquals(lastName, "Михайлович")
                assertEquals(grade, "")
                assertEquals(chair, "33/12 Кафедра \"Металлургические и литейные технологии\"")
            }
        }
    }
}