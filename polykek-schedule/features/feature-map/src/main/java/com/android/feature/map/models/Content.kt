package com.android.feature.map.models

/**
 * Content model for map toolbar item.
 *
 * @property title Title
 * @property subTitle1 Subtitle 1
 * @property subTitle2 Subtitle 2
 * @property subTitle3 Subtitle 3
 * @property opposingTitle Opposing title
 * @constructor Create [Content]
 */
internal data class Content(
    val title: String? = null,
    val subTitle1: String? = null,
    val subTitle2: String? = null,
    val subTitle3: String? = null,
    val opposingTitle: String? = null
)