package com.android.feature.map.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.yandex.runtime.image.ImageProvider

/**
 * Yandex-map drawable image provider. Their lib still doesn't support vector images - (╯°益°)╯彡┻━┻.
 *
 * @property context App context
 * @property id Drawable id
 * @constructor Create [DrawableImageProvider]
 */
internal class DrawableImageProvider(
    private val context: Context?,
    @DrawableRes val id: Int
) : ImageProvider() {
    override fun getImage() = context?.getBitmapFromVectorDrawable(id)

    override fun getId() = id.toString()

    /**
     * Get bitmap from vector drawable.
     *
     * @receiver [Context]
     * @param drawableId Drawable id
     * @return [Bitmap] or null
     */
    private fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}