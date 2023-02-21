package com.android.core.ui.navigation.polytechCicirone

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.core.ui.R
import com.android.shared.code.utils.syntaxSugar.isPortraitMode
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen

/**
 * Polytech app navigator provides custom work with navigation.
 *
 * @constructor Create [PolytechAppNavigator]
 *
 * @param activity Single activity
 * @param containerId Main container id
 * @param fragmentManager Activity fragment manager
 * @param fragmentFactory Activity fragment factory
 */
class PolytechAppNavigator (
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager = activity.supportFragmentManager,
    fragmentFactory: FragmentFactory = fragmentManager.fragmentFactory
) : AppNavigator(
    activity,
    containerId,
    fragmentManager,
    fragmentFactory
) {
    override fun commitNewFragmentScreen(screen: FragmentScreen, addToBackStack: Boolean) {
        if (screen is PolytechFragmentScreen) {
            with(fragmentManager) {
                beginTransaction().apply {
                    setCustomAnimation(screen.animationType, activity.isPortraitMode())
                    val instance = screen.createFragment(fragmentFactory)
                    screen.arguments?.let { instance.arguments = it }
                    replace(containerId, instance, screen.screenKey)
                    // Add to the backstack.
                    if (screen.addToBackStack) {
                        addToBackStack(screen.screenKey)
                        localStackCopy.add(screen.screenKey)
                    }
                    setReorderingAllowed(true)
                    commit()
                }
            }
        } else {
            super.commitNewFragmentScreen(screen, addToBackStack)
        }
    }

    /**
     * Set custom animation: horizontal sliding - for portrait mode and vertical - for landscape.
     *
     * @receiver [FragmentTransaction]
     * @param animationType Animation type
     * @param isPortraitMode Is portrait mode
     */
    private fun FragmentTransaction.setCustomAnimation(animationType: AnimationType, isPortraitMode: Boolean) {
        when {
            animationType == AnimationType.FROM_RIGHT_TO_LEFT && isPortraitMode -> setCustomAnimations(
                R.anim.right_animation_enter,
                R.anim.right_animation_leave,
                R.anim.left_animation_enter,
                R.anim.left_animation_leave
            )
            animationType == AnimationType.FROM_LEFT_TO_RIGHT && isPortraitMode -> setCustomAnimations(
                R.anim.left_animation_enter,
                R.anim.left_animation_leave,
                R.anim.right_animation_leave,
                R.anim.right_animation_enter
            )
            animationType == AnimationType.FROM_RIGHT_TO_LEFT -> setCustomAnimations(
                R.anim.top_animation_enter,
                R.anim.top_animation_leave,
                R.anim.bottom_animation_enter,
                R.anim.bottom_animation_leave
            )
            animationType == AnimationType.FROM_LEFT_TO_RIGHT -> setCustomAnimations(
                R.anim.bottom_animation_enter,
                R.anim.bottom_animation_leave,
                R.anim.top_animation_leave,
                R.anim.top_animation_enter
            )
            else -> { /* do nothing */ }
        }
    }
}