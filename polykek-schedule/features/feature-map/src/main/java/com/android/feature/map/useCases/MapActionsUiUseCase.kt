package com.android.feature.map.useCases

import android.content.Context
import com.android.feature.map.models.YandexMapItem
import com.android.feature.map.utils.DrawableImageProvider
import com.android.shared.code.utils.markers.IUseCase
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
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
     * Focus map on [boundingBox] area.
     *
     * @param yandexMap Yandex map
     * @param boundingBox Bounding box
     */
    internal fun move(yandexMap: Map?, boundingBox: BoundingBox?) {
        if (yandexMap != null && boundingBox != null) {
            yandexMap.move(
                yandexMap.cameraPosition(
                    Geometry.fromBoundingBox(boundingBox),
                ),
                Animation(Animation.Type.SMOOTH, DEFAULT_DURATION),
                null
            )
        }
    }

    /**
     * Move.
     *
     * @param yandexMap Yandex map
     * @param point Point
     */
    internal fun move(yandexMap: Map?, point: Point) {
        yandexMap?.move(
            CameraPosition(point, DEFAULT_ZOOM, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, DEFAULT_DURATION),
            null
        )
    }

    /**
     * Move.
     *
     * @param yandexMap Yandex map
     * @param cameraPosition Camera position
     */
    internal fun move(yandexMap: Map?, cameraPosition: CameraPosition?) {
        if (yandexMap != null && cameraPosition != null) {
            yandexMap.move(cameraPosition, Animation(Animation.Type.SMOOTH, DEFAULT_DURATION), null)
        }
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
        val mapObject = yandexMap.mapObjects.addPlacemark().apply {
            geometry = yandexMapItem.point
            setIcon(DrawableImageProvider(context?.applicationContext, yandexMapItem.imageId))
        }
        mapObject.userData = yandexMapItem.userData
        mapObject.addTapListener(circleMapObjectTapListener)
    }
}