package com.android.core.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.android.core.ui.mvi.MviAction
import com.android.core.ui.mvi.MviIntent
import com.android.core.ui.mvi.MviState
import com.android.core.ui.mvi.MviViewModel
import com.android.core.ui.view.custom.PolytechToolbar

/**
 * Base fragment for all, which contains toolbar and menu. Generally there are root-feature-fragments.
 *
 * @constructor Create empty constructor for toolbar fragment
 */
abstract class ToolbarFragment<I : MviIntent, S : MviState, A : MviAction, VM : MviViewModel<I, S, A>> :
    NavigationFragment<I, S, A, VM>(), MenuProvider {
    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

    /**
     * On menu item selected by activity menu host.
     *
     * @param item Item
     * @return Return false to allow normal menu processing to proceed, true to consume it here
     */
    override fun onMenuItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            tabRouter?.exit() ?: mainRouter.exit()
            return true
        }
        return false
    }

    /**
     * Update toolbar.
     *
     * @receiver [PolytechToolbar]
     * @param titleId Title id
     * @param showBackBtn Show back btn
     */
    protected fun PolytechToolbar.updateToolbar(@StringRes titleId: Int, showBackBtn: Boolean = false) =
        updateToolbar(getString(titleId), showBackBtn)

    /**
     * Update toolbar.
     *
     * @receiver [PolytechToolbar]
     * @param title Title
     * @param showBackBtn Show back btn
     */
    protected fun PolytechToolbar.updateToolbar(title: String?, showBackBtn: Boolean = false) {
        with(activity as AppCompatActivity) {
            setSupportActionBar(this@updateToolbar)
            supportActionBar?.title = title
            supportActionBar?.setDisplayHomeAsUpEnabled(showBackBtn)
        }
    }
}