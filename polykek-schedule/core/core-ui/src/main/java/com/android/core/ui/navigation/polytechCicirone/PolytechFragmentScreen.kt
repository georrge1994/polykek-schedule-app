package com.android.core.ui.navigation.polytechCicirone

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.Creator
import com.github.terrakok.cicerone.androidx.FragmentScreen

/**
 * Extension [FragmentScreen] with additional polytech app properties.
 */
interface PolytechFragmentScreen : FragmentScreen {
    val addToBackStack: Boolean
    val animationType: AnimationType
    val arguments: Bundle?

    companion object {
        /**
         * Invoke.
         *
         * @param tag Tag
         * @param addToBackStack Add to back stack
         * @param animationType Animation type
         * @param arguments Arguments
         * @param fragmentCreator Fragment creator
         * @return [PolytechFragmentScreen]
         */
        operator fun invoke(
            tag: String? = null,
            addToBackStack: Boolean = true,
            animationType: AnimationType = AnimationType.FROM_RIGHT_TO_LEFT,
            arguments: Bundle? = null,
            fragmentCreator: Creator<FragmentFactory, Fragment>
        ) = object : PolytechFragmentScreen {
            override val screenKey = tag ?: fragmentCreator::class.java.name
            override val clearContainer = true
            override val addToBackStack: Boolean = addToBackStack
            override val animationType: AnimationType = animationType
            override val arguments: Bundle? = arguments
            override fun createFragment(factory: FragmentFactory) = fragmentCreator.create(factory)
        }
    }
}