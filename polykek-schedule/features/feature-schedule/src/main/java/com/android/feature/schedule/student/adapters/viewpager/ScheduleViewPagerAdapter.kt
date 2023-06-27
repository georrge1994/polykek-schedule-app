package com.android.feature.schedule.student.adapters.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.android.core.ui.adapters.BaseFragmentStateAdapter
import com.android.feature.schedule.student.fragments.DayFragment

private const val NUM_PAGES = 6

/**
 * Schedule view pager adapter.
 *
 * @constructor Create [ScheduleViewPagerAdapter]
 *
 * @param fragmentManager Child fragment manager
 * @param viewLifecycle Lifecycle of view
 */
internal class ScheduleViewPagerAdapter(
    fragmentManager: FragmentManager,
    viewLifecycle: Lifecycle
) : BaseFragmentStateAdapter(fragmentManager, viewLifecycle) {
    override fun getItemCount() = NUM_PAGES

    override fun createFragment(position: Int): Fragment = DayFragment.newInstance(position)

    override fun shouldBeDetached(fragment: Fragment): Boolean = fragment is DayFragment
}