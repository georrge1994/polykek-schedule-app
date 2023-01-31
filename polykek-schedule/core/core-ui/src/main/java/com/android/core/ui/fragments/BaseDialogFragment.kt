package com.android.core.ui.fragments

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Base dialog fragment.
 *
 * @constructor Create empty constructor for base dialog fragment
 */
abstract class BaseDialogFragment : DialogFragment() {
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