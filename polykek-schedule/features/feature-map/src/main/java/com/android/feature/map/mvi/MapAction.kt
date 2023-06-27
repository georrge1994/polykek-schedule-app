package com.android.feature.map.mvi

import com.android.core.ui.mvi.MviAction
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

/**
 * Map action.
 *
 * @constructor Create empty constructor for map action
 */
internal sealed class MapAction : MviAction {
    /**
     * Reset map to default area.
     *
     * @property boundingBox Area with all day-markers
     * @constructor Create [DefaultFocus]
     */
    internal data class DefaultFocus(val boundingBox: BoundingBox) : MapAction()

    /**
     * Show the last saved camera position (actual for recreating UI cases).
     *
     * @property cameraPosition Last [CameraPosition] before UI-destroying
     * @constructor Create [UpdateCameraPosition]
     */
    internal data class UpdateCameraPosition(val cameraPosition: CameraPosition) : MapAction()

    /**
     * Show building search on the map.
     *
     * @property point Point of map item
     * @constructor Create [ShowBuildingSearchOnTheMap]
     */
    internal data class ShowBuildingSearchOnTheMap(val point: Point) : MapAction()

    /**
     * Show building screen.
     */
    internal object ShowBuildingScreen : MapAction()
}