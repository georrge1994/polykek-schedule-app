package com.android.schedule.controller.impl.useCases

import com.android.schedule.controller.impl.support.dataGenerator.HarryPotterLessonMockkDataGenerator
import com.android.schedule.controller.impl.support.dataGenerator.LessonDataGenerator
import com.android.test.support.unitTest.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Harry potter joker use case test for [HarryPotterJokerUseCase].
 *
 * @constructor Create empty constructor for harry potter joker use case test
 */
class HarryPotterJokerUseCaseTest : BaseUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val harryPotterJokerUseCase = HarryPotterJokerUseCase()
    private val harryPotterLessonMockkDataGenerator = HarryPotterLessonMockkDataGenerator()

    /**
     * Replace lessons with harry potter memes null test.
     */
    @Test
    fun replaceLessonsWithHarryPotterMemes_nullTest() {
        assertEquals(
            null,
            harryPotterJokerUseCase.replaceLessonsWithHarryPotterMemes(groupId = 1023, weekResponse = null)
        )
    }

    /**
     * Replace lessons with harry potter memes complex.
     */
    @Test
    fun replaceLessonsWithHarryPotterMemes_complex() {
        assertEquals(
            harryPotterLessonMockkDataGenerator.getHarryPotterVariant(),
            harryPotterJokerUseCase.replaceLessonsWithHarryPotterMemes(
                groupId = 1023,
                weekResponse = lessonDataGenerator.getComplexWeekResponse()
            )
        )
    }
}