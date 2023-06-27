package com.android.feature.notes.fragments

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.SearchToolbarFragment
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.notes.R
import com.android.feature.notes.adapters.viewPager.NotesViewPagerAdapter
import com.android.feature.notes.dagger.NotesComponentHolder
import com.android.feature.notes.databinding.FragmentNotesBinding
import com.android.feature.notes.mvi.NotesAction
import com.android.feature.notes.mvi.NotesIntent
import com.android.feature.notes.mvi.NotesState
import com.android.feature.notes.viewModels.NotesViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.ui.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Notes fragment contains lists of lesson and own notes. THe 4th tab on the main screen.
 *
 * @constructor Create empty constructor for notes fragment
 */
internal class NotesFragment : SearchToolbarFragment<NotesIntent, NotesState, NotesAction, NotesViewModel>(
    NotesViewModel::class,
) {
    private val viewBinding by viewBinding(FragmentNotesBinding::bind)
    private lateinit var zoomOutPageTransformer: ZoomOutPageTransformer

    override val menuId: Int = R.menu.menu_search_notes

    private val addOwnNoteListener = MenuItem.OnMenuItemClickListener {
        NotesIntent.OpenNoteEditorNew.dispatchIntent()
        true
    }

    private val removeNotesListener = MenuItem.OnMenuItemClickListener {
        NotesIntent.DeleteSelectedNotes.dispatchIntent()
        true
    }

    private val viewPagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            NotesIntent.ChangeTab(position).dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = NotesComponentHolder.getComponent()

    override fun injectToComponent() = NotesComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        zoomOutPageTransformer = ZoomOutPageTransformer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_notes, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewModel.asyncSubscribe()

        viewBinding.toolbarLayout.toolbar.updateToolbar(R.string.notes_fragment_notes, false)
        viewBinding.viewPager2.adapter = NotesViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        viewBinding.viewPager2.setPageTransformer(zoomOutPageTransformer)
        viewBinding.viewPager2.isSaveEnabled = false
        viewBinding.viewPager2.registerOnPageChangeCallback(viewPagerListener)
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> context?.getString(R.string.notes_fragment_tab_title_by_lessons)
                1 -> context?.getString(R.string.notes_fragment_tab_title_own_notes)
                else -> context?.getString(R.string.notes_fragment_tab_title_own_notes)
            }
        }.attach()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateMenu(menu, menuInflater)
        TransitionManager.beginDelayedTransition(viewBinding.toolbarLayout.toolbar)
        menu.findItem(R.id.removeNotes).isVisible = viewModel.currentState.isSelectionModeEnabled
        menu.findItem(R.id.addNote).setOnMenuItemClickListener(addOwnNoteListener)
        menu.findItem(R.id.removeNotes).setOnMenuItemClickListener(removeNotesListener)
    }

    override fun getSearchIntent(keyWord: String?): NotesIntent = NotesIntent.KeyWordChanged(keyWord)

    override fun invalidateUi(state: NotesState) {
        super.invalidateUi(state)
        if (viewBinding.viewPager2.currentItem != state.tabPosition) {
            viewBinding.viewPager2.setCurrentItem(state.tabPosition, false)
        }
    }

    override fun executeSingleAction(action: NotesAction) {
        super.executeSingleAction(action)
        if (action is NotesAction.OpenNoteEditorNew) {
            openNoteEditor(action.selectedItemId)
        } else if (action is NotesAction.UpdateToolbar) {
            activity?.invalidateOptionsMenu()
        }
    }

    /**
     * Open note editor.
     *
     * @param selectedItemId Selected group/professor id
     */
    private fun openNoteEditor(selectedItemId: Int?) = tabRouter?.navigateTo(
        PolytechFragmentScreen {
            NoteEditorFragment.newInstance(selectedItemId)
        }
    )

    override fun onDestroyView() {
        viewBinding.viewPager2.unregisterOnPageChangeCallback(viewPagerListener)
        viewBinding.viewPager2.adapter = null
        super.onDestroyView()
        viewModel.unSubscribe()
    }
}