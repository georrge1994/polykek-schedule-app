package com.android.core.ui.view.custom

import android.content.Context
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.view.ContextThemeWrapper
import android.widget.TextView
import androidx.annotation.StyleRes
import com.android.core.ui.view.ext.setRandomViewId
import com.codesgood.views.JustifiedTextView

/**
 * Samsung has a bug with [JustifiedTextView], so we need to use a normal TextView for it.
 * In the future I will update all UI to Compose, so this will be a quick simple solution.
 *
 * @param context Context
 * @param themeResId Int
 * @return TextView
 */
fun getJustifiedTextView(
    context: Context,
    @StyleRes themeResId: Int
): TextView = if (android.os.Build.MANUFACTURER == "samsung") {
    TextView(ContextThemeWrapper(context, themeResId)).setRandomViewId().apply {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
    }
} else {
    JustifiedTextView(ContextThemeWrapper(context, themeResId))
}