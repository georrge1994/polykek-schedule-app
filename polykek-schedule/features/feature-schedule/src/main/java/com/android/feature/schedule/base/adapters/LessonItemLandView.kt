package com.android.feature.schedule.base.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.android.common.models.schedule.Lesson
import com.android.feature.schedule.R

/**
 * Lesson item landscape view. Used to avoid inflating process on the fly.
 *
 * @constructor Create [LessonItemLandView]
 *
 * @param context Context
 */
internal class LessonItemLandView(context: Context) : LessonItemBaseView(context) {
    private val time: AppCompatTextView = AppCompatTextView(ContextThemeWrapper(context, R.style.Small_Label_Style)).addToParent()
    private val title: AppCompatTextView = AppCompatTextView(ContextThemeWrapper(context, R.style.Bold_Label_Style)).addToParent()
    private val typeAndAddress: AppCompatTextView = AppCompatTextView(ContextThemeWrapper(context, R.style.Small_Label_Style)).addToParent()
    private val teacherName: AppCompatTextView = AppCompatTextView(ContextThemeWrapper(context, R.style.Small_Label_Style)).addToParent()
    private val withNotes: AppCompatImageView = AppCompatImageView(context).addToParent()
    private val more: AppCompatImageView = AppCompatImageView(context).addToParent()

    init {
        initParentLayout()
        initTimeTextView()
        initTitleTextView()
        initTypeAndAddressTextView()
        initTeacherNameTextView()
        initWithNotes()
        initMoreIcon()
    }

    @SuppressLint("SetTextI18n")
    override fun bindData(lesson: Lesson, listener: ILessonActions?) {
        time.text = lesson.time
        title.text = lesson.title
        typeAndAddress.text = "${lesson.typeLesson} - ${lesson.address}"
        teacherName.text = lesson.teacherNames
        more.isVisible = listener != null
        withNotes.isVisible = lesson.withNotes
        if (listener != null)
            parentLayout.setOnClickListener { listener.onClick(lesson) }
    }

    /**
     * Init parent layout.
     */
    private fun initParentLayout() = with(parentLayout) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        background = ResourcesCompat.getDrawable(resources, R.drawable.selector_background_items, context.theme)
    }

    /**
     * Init time view.
     */
    private fun initTimeTextView() = with(time) {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            val marginTop = resources.getDimensionPixelSize(R.dimen.padding_big)
            val marginStart = resources.getDimensionPixelSize(R.dimen.padding_big)
            setMargins(marginStart, marginTop, 0, 0)
            startToStart = parentLayout.id
            topToTop = parentLayout.id
        }
    }

    /**
     * Init title view.
     */
    private fun initTitleTextView() = with(title) {
        layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).apply {
            val marginTop = resources.getDimensionPixelSize(R.dimen.margin_item_tiny)
            setMargins(0, marginTop, 0, 0)
            startToEnd = time.id
            topToTop = parentLayout.id
            endToStart = more.id
        }
        val padding = resources.getDimensionPixelSize(R.dimen.padding_middle)
        setPadding(padding, 0, padding, 0)
    }

    /**
     * Init type and address view.
     */
    private fun initTypeAndAddressTextView() = with(typeAndAddress) {
        layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).apply {
            val marginTop = resources.getDimensionPixelSize(R.dimen.margin_item_tiny)
            setMargins(0, marginTop, 0, 0)
            startToStart = title.id
            topToBottom = title.id
            endToEnd = title.id
        }
        val padding = resources.getDimensionPixelSize(R.dimen.padding_middle)
        setPadding(padding, 0, padding, 0)
    }

    /**
     * Init teacher name view.
     */
    private fun initTeacherNameTextView() = with(teacherName) {
        layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).apply {
            val marginHorizontal = resources.getDimensionPixelSize(R.dimen.margin_item_tiny)
            setMargins(0, marginHorizontal, 0, 0)
            startToStart = title.id
            topToBottom = typeAndAddress.id
            endToEnd = title.id
        }
        val padding = resources.getDimensionPixelSize(R.dimen.padding_middle)
        val paddingBottom = resources.getDimensionPixelSize(R.dimen.padding_tiny)
        setPadding(padding, 0, padding, paddingBottom)
    }

    /**
     * Init with notes view.
     */
    private fun initWithNotes() = with(withNotes) {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            startToStart = time.id
            endToEnd = time.id
            topToBottom = time.id
        }
        background = ResourcesCompat.getDrawable(resources, R.drawable.ic_star_border_black_24dp, context.theme)
        visibility = View.GONE
    }

    /**
     * Init more icon view.
     */
    private fun initMoreIcon() = with(more) {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            val endMargin = resources.getDimensionPixelSize(R.dimen.margin_item_big)
            setMargins(0, 0, endMargin, 0)
            topToTop = parentLayout.id
            endToEnd = parentLayout.id
            bottomToBottom = parentLayout.id
        }
        background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_forward_ios_24, context.theme)
        visibility = View.GONE
    }
}