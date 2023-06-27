package com.android.feature.map.models

import com.android.feature.map.repositories.MapPoints
import com.yandex.mapkit.geometry.BoundingBox

/**
 * Map instruction.
 *
 * @property yandexMapItems List of [YandexMapItem]
 * @property isExistUnknownPlace Mean, that one or more addresses were not found in the [MapPoints]
 * @property boundingBox Object for zooming Yandex-map
 * @constructor Create [MapInstructions]
 */
internal data class MapInstructions(
    val yandexMapItems: List<YandexMapItem> = emptyList(),
    val isExistUnknownPlace: Boolean = false,
    val boundingBox: BoundingBox = BoundingBox()
)