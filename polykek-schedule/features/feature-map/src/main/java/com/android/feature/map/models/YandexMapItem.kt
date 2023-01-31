package com.android.feature.map.models

import androidx.annotation.DrawableRes
import com.android.common.models.map.Building
import com.android.common.models.schedule.Lesson
import com.yandex.mapkit.geometry.Point
import java.io.Serializable

/**
 * Yandex map item.
 *
 * @property point Point on the Yandex-Kit-Map
 * @property imageId Indicator icon res id
 * @property userData [Lesson] or [Building]
 * @constructor Create [YandexMapItem]
 */
internal data class YandexMapItem(
    val point: Point,
    @DrawableRes val imageId: Int,
    val userData: Any?
) : Serializable