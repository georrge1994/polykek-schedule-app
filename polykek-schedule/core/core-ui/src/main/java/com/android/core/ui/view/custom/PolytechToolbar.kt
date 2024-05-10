package com.android.core.ui.view.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import com.android.shared.code.utils.ui.SnowflakesEffect
import java.util.*

// Snow days.
private const val JANUARY_19 = 19
private const val DECEMBER_20 = 355

/**
 * Polytech toolbar contains some holiday features.
 *
 * @constructor Create [PolytechToolbar]
 *
 * @param context Context object
 * @param attrs View attributes
 */
class PolytechToolbar(context: Context, attrs: AttributeSet? = null) : Toolbar(context, attrs) {
    private val snowflakesEffect by lazy { SnowflakesEffect(context) }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        if (day <= JANUARY_19 || day > DECEMBER_20)
            snowflakesEffect.onDraw(this, canvas)
    }
}