package com.android.shared.code.utils.syntaxSugar

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Is portrait mode.
 *
 * @receiver [Context]
 * @return True if screen orientation is [Configuration.ORIENTATION_PORTRAIT]
 */
fun Context?.isPortraitMode(): Boolean {
    this ?: return false
    return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

/**
 * Hide status bar.
 */
@Suppress("DEPRECATION")
fun AppCompatActivity.hideStatusBar() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    window.insetsController?.hide(WindowInsets.Type.statusBars())
} else {
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

/**
 * Hide software keyboard.
 *
 * @receiver [View]
 */
fun View.hideSoftwareKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    clearFocus()
}