package com.android.core.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * This class helps to fix one of the [FragmentStateAdapter] bugs. Even you do everything right, the old fragments
 * from old adapter are not detached from fragment manager. This class helps to fix this bug.
 *
 * Link to issue: https://github.com/android/views-widgets-samples/issues/257
 *
 * @property fragmentManager Child fragment manager
 * @constructor Create [BaseFragmentStateAdapter]
 *
 * @param viewLifecycle Lifecycle of view
 */
abstract class BaseFragmentStateAdapter(
    private val fragmentManager: FragmentManager,
    viewLifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, viewLifecycle) {
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        fragmentManager.beginTransaction().apply {
            fragmentManager.fragments.forEach { fragment ->
                if (shouldBeDetached(fragment)) {
                    detach(fragment)
                }
            }
        }.commitAllowingStateLoss()
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        fragmentManager.beginTransaction().apply {
            fragmentManager.fragments.forEach { fragment ->
                if (shouldBeDetached(fragment)) {
                    detach(fragment)
                }
            }
        }.commitAllowingStateLoss()
    }

    /**
     * Should be detached.
     *
     * @param fragment Fragment
     * @return True if fragment should be detached
     */
    protected abstract fun shouldBeDetached(fragment: Fragment): Boolean
}