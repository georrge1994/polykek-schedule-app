package com.android.feature.schools.api

import com.android.common.models.api.Resource
import com.android.test.support.unitTest.base.BaseApiRepositoryTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * School api repository test for [SchoolApiRepository].
 *
 * @constructor Create empty constructor for school api repository test
 */
class SchoolApiRepositoryTest : BaseApiRepositoryTest() {
    private val schoolApiRepository = SchoolApiRepository(getApi(SchoolApiService::class.java))

    /**
     * Get schools 404.
     */
    @Test
    fun getSchools_404() = runTest {
        enqueueResponse(code = 404)
        schoolApiRepository.getSchools().apply {
            assertEquals("Client Error", message)
            assertEquals(null, data)
        }
    }

    /**
     * Get buildings 200.
     */
    @Test
    fun getBuildings_200() = runTest {
        enqueueResponse(fileName = "faculties-200.json", code = 200)
        schoolApiRepository.getSchools().apply {
            assert(this is Resource.Success)
            assert(data?.faculties?.isNotEmpty() == true)
            this.data?.faculties?.first()?.apply {
                assertEquals(id, "124")
                assertEquals(name, "Физико-механический институт")
                assertEquals(abbr, "ФизМех")
            }
        }
    }
}