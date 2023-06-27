package com.android.feature.faq.models

import androidx.annotation.StringRes

/**
 * FAQ.
 *
 * @property questionId Question resource id
 * @property answerId Answer resource id
 * @property isOpened Is answer visible
 * @constructor Create [FAQ]
 */
internal data class FAQ(
    @StringRes val questionId: Int,
    @StringRes val answerId: Int,
    val isOpened: Boolean = false
)