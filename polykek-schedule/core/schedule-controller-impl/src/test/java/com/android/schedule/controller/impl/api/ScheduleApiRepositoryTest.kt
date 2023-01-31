package com.android.schedule.controller.impl.api

import com.android.common.models.api.Resource
import com.android.test.support.unitTest.base.BaseApiRepositoryTest
import com.android.test.support.unitTest.checkNegativeResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Schedule api repository test for [ScheduleApiRepository].
 *
 * @constructor Create empty constructor for schedule api repository test
 */
class ScheduleApiRepositoryTest : BaseApiRepositoryTest() {
    private val scheduleApiRepository = ScheduleApiRepository(scheduleApiService = getApi(ScheduleApiService::class.java))

    /**
     * Get schedule for group 404.
     */
    @Test
    fun getScheduleForGroup_404() = runTest {
        enqueueResponse(code = 404)
        scheduleApiRepository.getScheduleForGroup(2559, "2020-06-08").checkNegativeResult()
    }

    /**
     * Get schedule for group 200.
     */
    @Test
    fun getScheduleForGroup_200() = runTest {
        enqueueResponse(fileName = "schedule-200.json", code = 200)
        scheduleApiRepository.getScheduleForGroup(2559, "2020-06-08").apply {
            assert(this is Resource.Success)
            assertEquals("2020.06.08", data?.weekInfo?.dateStart)
            assertEquals("2020.06.14", data?.weekInfo?.dateEnd)
            assert(data?.weekInfo?.isOdd == false)
            assert(data?.days?.isEmpty() == true)
        }
    }

    /**
     * Get professor schedule 404.
     */
    @Test
    fun getProfessorSchedule_404() = runTest {
        enqueueResponse(code = 404)
        scheduleApiRepository.getProfessorSchedule(1, "2020-06-08").checkNegativeResult()
    }

    /**
     * Get professor schedule 200.
     */
    @Test
    fun getProfessorSchedule_200() = runTest {
        enqueueResponse(fileName = "schedule-200.json", code = 200)
        scheduleApiRepository.getProfessorSchedule(1, "2020-06-08").apply {
            assert(this is Resource.Success)
            assertEquals("2020.06.08", data?.weekInfo?.dateStart)
            assertEquals("2020.06.14", data?.weekInfo?.dateEnd)
            assert(data?.weekInfo?.isOdd == false)
            assert(data?.days?.isEmpty() == true)
        }
    }
}