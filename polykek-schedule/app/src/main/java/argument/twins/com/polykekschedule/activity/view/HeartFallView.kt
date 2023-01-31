package argument.twins.com.polykekschedule.activity.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import argument.twins.com.polykekschedule.R
import com.android.shared.code.utils.ui.HeartsEffect

/**
 * Heart fall view.
 *
 * @constructor Create [HeartFallView]
 *
 * @param context Context
 */
class HeartFallView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr
) {
    private val heartsEffect = HeartsEffect(context)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
        elevation = resources.getDimensionPixelSize(R.dimen.app_elevation).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        heartsEffect.onDraw(this, canvas)
    }
}