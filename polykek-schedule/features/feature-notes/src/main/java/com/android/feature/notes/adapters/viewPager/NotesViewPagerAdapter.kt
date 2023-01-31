package com.android.feature.notes.adapters.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.feature.notes.fragments.NoteListFragment
import com.android.feature.notes.models.NotesTabTypes

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
) : FragmentStateAdapter(fragmentManager, viewLifecycle) {
    override fun getItemCount() = NUM_PAGES

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> NoteListFragment.newInstance(NotesTabTypes.BY_LESSONS)
        1 -> NoteListFragment.newInstance(NotesTabTypes.OWN_NOTES)
        else -> NoteListFragment.newInstance(NotesTabTypes.OWN_NOTES)
    }
}