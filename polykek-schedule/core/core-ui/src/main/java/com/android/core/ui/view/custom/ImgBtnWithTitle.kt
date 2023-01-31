package com.android.core.ui.view.custom

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.android.core.ui.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Custom view - scalable image with text.
 *
 * @constructor Create [ImgBtnWithTitle]
 *
 * @param context Context object
 * @param attrs View attributes
 * @param defStyleAttr Default style attributes
 */
class ImgBtnWithTitle(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(
    context,
    attrs,
    defStyleAttr
) {
    private var imageView: ShapeableImageView? = null
    private var textView: TextView? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        setBackgroundResource(R.drawable.shadow_background_small)
        orientation = VERTICAL
        // Fetch settings from attrs.
        context.obtainStyledAttributes(attrs, R.styleable.ImgBtnWithTitle).apply {
            val drawableRes = getResourceId(R.styleable.ImgBtnWithTitle_src, 0)
            if (drawableRes != 0) {
                imageView = getShapeableImageView(drawableRes)
                addView(imageView)
            }
            val title = getString(R.styleable.ImgBtnWithTitle_title)
            if (title != null) {
                textView = getTextView(title)
                addView(textView)
            }
            recycle()
            isClickable = true
            isFocusable = true
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val drawableSize = min(w, (0.75f * h).toInt())
        // Update parent layout.
        val parentPadding = (0.08 * drawableSize).roundToInt()
        this.setPadding(parentPadding, parentPadding, parentPadding, parentPadding)
        // Update size of image.
        imageView?.layoutParams = LayoutParams(drawableSize, drawableSize)
        // Update size of label.
        textView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, 0.08f * h)
        textView?.setPadding(0, (0.02 * h).roundToInt(), 0, 0)
        // Refresh UI.
        CoroutineScope(Dispatchers.Main + Job()).launch {
            requestLayout()
        }
    }

    /**
     * Get shapeable image view.
     *
     * @param drawableId Drawable id
     * @return [ShapeableImageView]
     */
    private fun getShapeableImageView(@DrawableRes drawableId: Int): ShapeableImageView = ShapeableImageView(context).apply {
        gravity = Gravity.CENTER
        setImageDrawable(ContextCompat.getDrawable(context, drawableId))
        scaleType = ImageView.ScaleType.CENTER_CROP
        // Made the rounded corners.
        val radius = resources.getDimension(R.dimen.default_corner_radius)
        shapeAppearanceModel = shapeAppearanceModel.toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()
        // Make it not clickable.
        isClickable = false
        isFocusable = false
    }

    /**
     * Get text view.
     *
     * @param title Title
     * @return [TextView]
     */
    private fun getTextView(title: String): TextView = TextView(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        gravity = Gravity.CENTER
        text = title
        // Make it not clickable.
        isClickable = false
        isFocusable = false
    }
}