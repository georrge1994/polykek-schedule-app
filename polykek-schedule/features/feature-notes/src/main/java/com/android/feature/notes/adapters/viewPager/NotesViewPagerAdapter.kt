package com.android.feature.notes.adapters.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.android.core.ui.adapters.BaseFragmentStateAdapter
import com.android.feature.notes.fragments.NoteListFragment
import com.android.feature.notes.models.NotesTabType

private const val NUM_PAGES = 2

/**
 * Notes view pager adapter.
 *
 * @constructor Create [NotesViewPagerAdapter]
 *
 * @param fragmentManager Child fragment manager
 * @param viewLifecycle Lifecycle of view
 */
internal class NotesViewPagerAdapter(
    fragmentManager: FragmentManager,
    viewLifecycle: Lifecycle
) : BaseFragmentStateAdapter(fragmentManager, viewLifecycle) {
    override fun getItemCount() = NUM_PAGES

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> NoteListFragment.newInstance(NotesTabType.BY_LESSONS)
        1 -> NoteListFragment.newInstance(NotesTabType.OWN_NOTES)
        else -> NoteListFragment.newInstance(NotesTabType.OWN_NOTES)
    }

    override fun shouldBeDetached(fragment: Fragment): Boolean = fragment is NoteListFragment
}