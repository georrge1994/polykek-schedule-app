package com.android.feature.groups.api

import com.android.common.models.api.Resource
import com.android.test.support.unitTest.base.BaseApiRepositoryTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Groups api repository test.
 *
 * @constructor Create empty constructor for groups api repository test
 */
class GroupsApiRepositoryTest : BaseApiRepositoryTest() {
    private val groupsApiRepository = GroupsApiRepository(groupApiService = getApi(GroupApiService::class.java))

    /**
     * Get groups 404.
     */
    @Test
    fun getGroups_404() = runTest {
        enqueueResponse(code = 404)
        groupsApiRepository.getGroups("100").apply {
            assertEquals("Client Error", message)
            assertEquals(null, data)
        }
    }

    /**
     * Get groups 200.
     */
    @Test
    fun getGroups_200() = runTest {
        enqueueResponse(fileName = "groups-200.json", code = 200)
        groupsApiRepository.getGroups("100").apply {
            assert(this is Resource.Success)
            assert(data?.groups?.isNotEmpty()== true)
            this.data?.groups?.first()?.apply {
                assertEquals(id, 35754)
                assertEquals(name, "3733806/00301")
                assertEquals(level, 3)
                assertEquals(type, "common")
                assertEquals(kind, "0")
                assertEquals(specialization, "38.03.06 Торговое дело")
            }
        }
    }
}