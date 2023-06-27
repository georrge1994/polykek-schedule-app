package com.android.feature.welcome.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.android.core.ui.adapters.BaseFragmentStateAdapter
import com.android.feature.welcome.fragments.ShootFragment

private const val NUM_PAGES = 4

/**
 * Welcome view pager adapter.
 *
 * @constructor Create [WelcomeViewPagerAdapter]
 *
 * @param fragmentManager Child fragment manager
 * @param viewLifecycle Lifecycle of view
 */
internal class WelcomeViewPagerAdapter(
    fragmentManager: FragmentManager,
    viewLifecycle: Lifecycle
) : BaseFragmentStateAdapter(fragmentManager, viewLifecycle) {
    override fun getItemCount() = NUM_PAGES

    override fun createFragment(position: Int): Fragment = ShootFragment.newInstance(position)

    override fun shouldBeDetached(fragment: Fragment): Boolean = fragment is ShootFragment
}