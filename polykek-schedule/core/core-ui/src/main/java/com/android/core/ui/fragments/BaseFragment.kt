package com.android.core.ui.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.module.injector.moduleMarkers.IModuleComponent
import javax.inject.Inject

/**
 * Base MVI-fragment.
 *
 * @constructor Create empty constructor for base fragment
 */
abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var strongReferenceToOwnComponent: IModuleComponent? = null

    override fun onAttach(context: Context) {
        // Every each top-level feature (screen feature) have to store a reference to the own component, because no one
        // before will not store it. We create feature-api only by navigation action, so we use dynamic dependencies
        // providers - no one feature will not keep reference to this api).
        strongReferenceToOwnComponent = getComponent()
        injectToComponent()
        super.onAttach(context)
    }

    /**
     * Get component.
     *
     * @return [IModuleComponent]
     */
    protected abstract fun getComponent(): IModuleComponent

    /**
     * Inject fragment to component.
     *
     * @return [Unit] or null
     */
    protected abstract fun injectToComponent(): Unit?

    override fun onDetach() {
        super.onDetach()
        // If fragment detached, we can drop reference to the component.
        strongReferenceToOwnComponent = null
    }
}
