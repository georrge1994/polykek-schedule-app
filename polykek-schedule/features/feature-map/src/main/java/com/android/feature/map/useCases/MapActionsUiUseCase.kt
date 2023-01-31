package com.android.feature.map.useCases

import android.content.Context
import com.android.feature.map.models.YandexMapItem
import com.android.feature.map.utils.DrawableImageProvider
import com.android.shared.code.utils.markers.IUseCase
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import javax.inject.Inject

private const val DEFAULT_ZOOM = 16.0f
private const val DEFAULT_DURATION = 0.5f

/**
 * This use case provides function to work with Yandex-map. Used separated class to avoid inflating the fragment code.
 *
 * @constructor Create empty constructor for map actions ui use case
 */
internal class MapActionsUiUseCase @Inject constructor() : IUseCase {
    /**
     * We have two focusing modes for map:
     * - All buildings - the default mode;
     * - Specific building - as a search result.
     * If we have a specific building focus, we have to block the default behaviour once. Next default focus requests will be allowed.
     * */
    private var isSearchResultExist = false

    /**
     * Focus map on [boundingBox] area.
     *
     * @param yandexMap Yandex map
     * @param boundingBox Bounding box
     */
    internal fun move(yandexMap: Map?, boundingBox: BoundingBox) {
        yandexMap ?: return
        if (!isSearchResultExist) {
            yandexMap.move(
                yandexMap.cameraPosition(boundingBox),
                Animation(Animation.Type.SMOOTH, DEFAULT_DURATION),
                null
            )
        } else {
            isSearchResultExist = false
        }
    }

    /**
     * Move.
     *
     * @param yandexMap Yandex map
     * @param point Point
     */
    internal fun move(yandexMap: Map?, point: Point) {
        yandexMap ?: return
        isSearchResultExist = true
        yandexMap.move(
            CameraPosition(point, DEFAULT_ZOOM, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, DEFAULT_DURATION),
            null
        )
    }

    /**
     * Add place mark.
     *
     * @param context Context
     * @param yandexMap Yandex map
     * @param yandexMapItem Yandex map item
     * @param circleMapObjectTapListener Circle map object tap listener
     */
    internal fun addPlaceMark(
        context: Context?,
        yandexMap: Map?,
        yandexMapItem: YandexMapItem,
        circleMapObjectTapListener: MapObjectTapListener
    ) {
        yandexMap ?: return
        val mapObject = yandexMap.mapObjects.addPlacemark(
            yandexMapItem.point,
            DrawableImageProvider(context, yandexMapItem.imageId)
        )
        mapObject.userData = yandexMapItem.userData
        mapObject.addTapListener(circleMapObjectTapListener)
    }
}