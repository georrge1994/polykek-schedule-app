package com.android.core.ui.fragments

import com.android.core.ui.mvi.MviAction
import com.android.core.ui.mvi.MviFragment
import com.android.core.ui.mvi.MviIntent
import com.android.core.ui.mvi.MviState
import com.android.core.ui.mvi.MviViewModel
import com.android.core.ui.navigation.ICiceroneHolder
import com.android.core.ui.navigation.IRouterProvider
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Navigation fragment.
 *
 * @constructor Create empty constructor for navigation fragment
 */
abstract class NavigationFragment<I : MviIntent, S : MviState, A : MviAction, VM : MviViewModel<I, S, A>> :
    MviFragment<I, S, A, VM>() {
    @Inject
    lateinit var ciceroneHolder: ICiceroneHolder

    protected val mainRouter: Router
        get() = ciceroneHolder.getMainCicerone().router

    protected val tabRouter: Router?
        get() = findTabRouterInAncestor()

    /**
     * Find tab router in ancestor.
     *
     * @return [Router] or null
     */
    private fun findTabRouterInAncestor(): Router? {
        var parent = parentFragment
        while (parent != null) {
            if (parent is IRouterProvider)
                return (parent as IRouterProvider).router
            parent = parent.parentFragment
        }
        return null
    }
}