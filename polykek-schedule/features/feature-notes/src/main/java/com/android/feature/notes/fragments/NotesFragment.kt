package com.android.feature.notes.fragments

import android.os.Bundle
import android.transition.TransitionManager
import android.view.*
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.SearchToolbarFragment
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.notes.R
import com.android.feature.notes.adapters.viewPager.NotesViewPagerAdapter
import com.android.feature.notes.dagger.NotesComponentHolder
import com.android.feature.notes.databinding.FragmentNotesBinding
import com.android.feature.notes.viewModels.NotesViewModel
import com.android.shared.code.utils.ui.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Notes fragment contains lists of lesson and own notes. THe 4th tab on the main screen.
 *
 * @constructor Create empty constructor for notes fragment
 */
internal class NotesFragment : SearchToolbarFragment<NotesViewModel>(NotesViewModel::class) {
    private val viewBinding by viewBinding(FragmentNotesBinding::bind)
    private lateinit var zoomOutPageTransformer: ZoomOutPageTransformer

    override val menuId: Int = R.menu.menu_search_notes

    private val addNoteListener = MenuItem.OnMenuItemClickListener {
        openNoteEditor()
        true
    }

    private val removeNotesListener = MenuItem.OnMenuItemClickListener {
        viewModel.deleteSelectedNotes()
        true
    }

    private val unSelectItemsObserver = Observer<Boolean> { activity?.invalidateOptionsMenu() }

    override fun injectToComponent() = NotesComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        zoomOutPageTransformer = ZoomOutPageTransformer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_notes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.toolbarLayout.toolbar.updateToolbar(R.string.notes_fragment_notes, false)
        viewBinding.viewPager2.adapter = NotesViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        viewBinding.viewPager2.setPageTransformer(zoomOutPageTransformer)
        viewBinding.viewPager2.isSaveEnabled = false
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> context?.getString(R.string.notes_fragment_tab_title_by_lessons)
                1 -> context?.getString(R.string.notes_fragment_tab_title_own_notes)
                else -> context?.getString(R.string.notes_fragment_tab_title_own_notes)
            }
        }.attach()

        viewModel.selectionModeState.observe(viewLifecycleOwner, unSelectItemsObserver)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateMenu(menu, menuInflater)
        TransitionManager.beginDelayedTransition(viewBinding.toolbarLayout.toolbar)
        menu.findItem(R.id.removeNotes).isVisible = viewModel.selectionModeState.value ?: false
        menu.findItem(R.id.addNote).setOnMenuItemClickListener(addNoteListener)
        menu.findItem(R.id.removeNotes).setOnMenuItemClickListener(removeNotesListener)
    }

    /**
     * Open note editor.
     */
    private fun openNoteEditor() = tabRouter?.navigateTo(
        PolytechFragmentScreen {
            NoteEditorFragment.newInstance(viewModel.selectedItemId)
        }
    )

    override fun onDestroyView() {
        viewBinding.viewPager2.adapter = null
        super.onDestroyView()
    }
}