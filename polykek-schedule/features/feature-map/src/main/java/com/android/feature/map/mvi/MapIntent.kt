package com.android.feature.map.mvi

import com.android.common.models.map.Building
import com.android.core.ui.mvi.MviIntent
import com.yandex.mapkit.map.CameraPosition

/**
 * Map intent.
 *
 * @constructor Create empty constructor for map intent
 */
internal sealed class MapIntent : MviIntent {
    /**
     * Show default focus.
     */
    internal data object MakeDefaultFocus : MapIntent()

    /**
     * Show next day.
     */
    internal data object ShowNextDay : MapIntent()

    /**
     * Show previous day.
     */
    internal data object ShowPreviousDay : MapIntent()

    /**
     * Show building screen.
     */
    internal data object ShowBuildingScreen : MapIntent()

    /**
     * Deselect map object.
     */
    internal data object DeselectMapObject : MapIntent()

    /**
     * Save camera position before UI-destroying.
     *
     * @property cameraPosition [CameraPosition]
     * @constructor Create [SaveCameraPosition]
     */
    internal data class SaveCameraPosition(val cameraPosition: CameraPosition) : MapIntent()

    /**
     * Select map object.
     *
     * @property userData Lesson or building information
     * @constructor Create [SelectMapObject]
     */
    internal data class SelectMapObject(val userData: Any?) : MapIntent()

    /**
     * Building search result.
     *
     * @property building [Building]
     * @constructor Create [BuildingSearchResult]
     */
    internal data class BuildingSearchResult(val building: Building?) : MapIntent()
}