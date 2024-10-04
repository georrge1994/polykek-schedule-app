package com.example.news.useCases

import com.android.test.support.androidTest.BaseAndroidUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [HtmlParserUseCase].
 */
class HtmlParserUseCaseTest : BaseAndroidUnitTest() {
    private val htmlParserUseCase = HtmlParserUseCase()

    @Test
    fun removeHtmlCodes_checkThatItWorks() {
        // Arrange
        val input = "<p>  This is a <b>test</b> string.  </p>"
        val expectedOutput = "This is a test string."

        // Act
        val output = htmlParserUseCase.removeHtmlCodes(input)

        // Assert
        assertEquals(expectedOutput, output)
    }

    @Test
    fun removeHtmlCodes_nullInputParam() {
        // Act
        val output = htmlParserUseCase.removeHtmlCodes(null)

        // Assert
        assertEquals("", output)
    }
}