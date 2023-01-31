package com.android.schedule.controller.impl.useCases

import com.android.schedule.controller.impl.support.dataGenerator.AudienceResponseDataGenerator
import com.android.test.support.androidTest.BaseAndroidUnitTest
import com.android.test.support.testFixtures.TEST_STRING
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Address aggregation use case test for [AddressAggregationUseCase].
 *
 * @constructor Create empty constructor for address aggregation use case test
 */
class AddressAggregationUseCaseTest : BaseAndroidUnitTest() {
    private val addressAggregationUseCase = AddressAggregationUseCase(application)
    private val audienceResponseDataGenerator = AudienceResponseDataGenerator()

    /**
     * Get corrected address no name.
     */
    @Test
    fun getCorrectedAddress_noName() {
        assertEquals(
            TEST_STRING,
            addressAggregationUseCase.getCorrectedAddress(audienceResponseDataGenerator.audienceResponseNoName)
        )
    }

    /**
     * Get corrected address 1.
     */
    @Test
    fun getCorrectedAddress1() {
        assertEquals(
            "DL, Дистанционно",
            addressAggregationUseCase.getCorrectedAddress(audienceResponseDataGenerator.audienceResponseDL)
        )
    }

    /**
     * Get corrected address 2.
     */
    @Test
    fun getCorrectedAddress2() {
        assertEquals(
            "3-й учебный корпус, test string",
            addressAggregationUseCase.getCorrectedAddress(audienceResponseDataGenerator.audienceResponse1)
        )
    }

    /**
     * Get combined correct address.
     */
    @Test
    fun getCombinedCorrectAddress() {
        assertEquals(
            "Гидротехнический корпус-1, I - test string; II - test string",
            addressAggregationUseCase.getCombinedCorrectAddress(
                audienceResponseDataGenerator.audienceResponse2,
                audienceResponseDataGenerator.audienceResponse3
            )
        )
    }

    /**
     * Get combined correct address nulls.
     */
    @Test
    fun getCombinedCorrectAddress_nulls() {
        assertEquals(
            TEST_STRING,
            addressAggregationUseCase.getCombinedCorrectAddress(null, null)
        )
    }
}