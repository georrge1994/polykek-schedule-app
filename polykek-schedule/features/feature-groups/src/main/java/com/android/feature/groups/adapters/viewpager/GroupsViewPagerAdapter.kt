package com.android.feature.groups.adapters.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.core.ui.models.ScheduleMode
import com.android.feature.groups.fragments.GroupListFragment
import com.android.feature.groups.models.GroupType

private const val NUM_PAGES = 3

/**
 * Groups view pager adapter.
 *
 * @property scheduleMode [ScheduleMode]
 * @constructor Create [GroupsViewPagerAdapter]
 *
 * @param fragmentManager Child fragment manager
 * @param viewLifecycle Lifecycle of view
 */
internal class GroupsViewPagerAdapter(
    private val scheduleMode: ScheduleMode,
    fragmentManager: FragmentManager,
    viewLifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, viewLifecycle) {
    override fun getItemCount() = NUM_PAGES

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> GroupListFragment.newInstance(scheduleMode, GroupType.BACHELOR)
        1 -> GroupListFragment.newInstance(scheduleMode, GroupType.MASTER)
        2 -> GroupListFragment.newInstance(scheduleMode, GroupType.OTHER)
        else -> GroupListFragment.newInstance(scheduleMode, GroupType.OTHER)
    }
}