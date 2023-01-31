package com.android.core.ui.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Base fragment.
 *
 * @constructor Create empty constructor for base fragment
 */
abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        injectToComponent()
        super.onAttach(context)
    }

    /**
     * Inject fragment to component.
     *
     * @return [Unit] or null
     */
    protected abstract fun injectToComponent(): Unit?
}
