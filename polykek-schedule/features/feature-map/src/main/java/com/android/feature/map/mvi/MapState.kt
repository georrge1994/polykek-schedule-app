package com.android.feature.map.mvi

import com.android.core.ui.mvi.MviState
import com.android.feature.map.models.Content
import com.android.feature.map.models.DayControls
import com.android.feature.map.models.YandexMapItem

/**
 * Map state properties.
 */
internal interface MapStateProperties {
    val content: Content?
    val yandexMapItems: List<YandexMapItem>
    val dayControls: DayControls?
    val searchResultMapItem: YandexMapItem?
}

/**
 * Map state.
 *
 * @constructor Create empty constructor for map state
 */
internal sealed class MapState : MviState, MapStateProperties {
    /**
     * Default state for map screen.
     */
    internal data object Default : MapState() {
        override val content: Content? = null
        override val yandexMapItems: List<YandexMapItem> = emptyList()
        override val dayControls: DayControls? = null
        override val searchResultMapItem: YandexMapItem? = null
    }

    /**
     * Update.
     *
     * @property content Content for map-toolbar
     * @property yandexMapItems Necessary lesson-map-items
     * @property dayControls State of day-controls
     * @property searchResultMapItem Additional map-item - the result of building search
     * @constructor Create [Update]
     */
    internal data class Update(
        override val content: Content?,
        override val yandexMapItems: List<YandexMapItem>,
        override val dayControls: DayControls?,
        override val searchResultMapItem: YandexMapItem?
    ) : MapState()

    /**
     * Copy state.
     *
     * @param content Content
     * @param searchResultMapItem Additional map-item - the result of building search
     * @param yandexMapItems Yandex map items
     * @param dayControls Day controls
     * @return [MapState]
     */
    internal fun copyState(
        content: Content? = this.content,
        searchResultMapItem: YandexMapItem? = this.searchResultMapItem,
        yandexMapItems: List<YandexMapItem> = this.yandexMapItems,
        dayControls: DayControls? = this.dayControls
    ): MapState = Update(
        content = content,
        yandexMapItems = yandexMapItems,
        dayControls = dayControls,
        searchResultMapItem = searchResultMapItem
    )
}