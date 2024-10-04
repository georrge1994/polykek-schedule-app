package com.example.news.useCases

import androidx.core.text.HtmlCompat
import javax.inject.Inject

/**
 * Html parser use case.
 *
 * @constructor Create instance of [HtmlParserUseCase]
 */
class HtmlParserUseCase @Inject constructor() {
    /**
     * Replace all html codes from a string.
     *
     * @text The string to remove html codes
     * @return The string without html codes
     */
    fun removeHtmlCodes(text: String?): String = text?.let {
        HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
    } ?: ""
}