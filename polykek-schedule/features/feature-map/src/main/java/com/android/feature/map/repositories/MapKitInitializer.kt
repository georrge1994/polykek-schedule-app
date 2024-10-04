package com.android.feature.map.repositories

import android.content.Context
import androidx.annotation.MainThread
import com.yandex.mapkit.MapKitFactory

private const val MAP_KIT_KEY = "80912269-7f79-4816-bf26-5e8205665ab2"

/**
 * Fix for https://github.com/yandex/mapkit-android-demo/issues/221.
 */
internal data object MapKitInitializer {
    private var initialized = false

    /**
     * Initialize.
     *
     * @param context Context
     */
    @MainThread
    internal fun initialize(context: Context) {
        if (initialized)
            return
        MapKitFactory.setApiKey(MAP_KIT_KEY)
        MapKitFactory.initialize(context)
        initialized = true
    }
}