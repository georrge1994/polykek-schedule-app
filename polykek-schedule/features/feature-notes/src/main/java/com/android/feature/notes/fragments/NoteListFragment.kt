package com.android.feature.notes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.NavigationFragment
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.notes.R
import com.android.feature.notes.adapters.recycler.INoteItemActions
import com.android.feature.notes.adapters.recycler.NoteItem
import com.android.feature.notes.adapters.recycler.NotesRecyclerViewAdapter
import com.android.feature.notes.dagger.NotesComponentHolder
import com.android.feature.notes.databinding.FragmentNoteListBinding
import com.android.feature.notes.models.NotesTabTypes
import com.android.feature.notes.viewModels.NotesViewModel
import com.android.shared.code.utils.syntaxSugar.createSharedViewModelWithParentFragment

private const val NOTE_TAB_TYPE = "NOTE_TAB_TYPE"

/**
 * Note list fragment contains note items and works in one of modes [NotesTabTypes].
 *
 * @constructor Create empty constructor for note list fragment
 */
internal class NoteListFragment : NavigationFragment() {
    private val viewBinding by viewBinding(FragmentNoteListBinding::bind)
    private lateinit var noteViewModel: NotesViewModel
    private lateinit var adapter: NotesRecyclerViewAdapter
    private var tabTypes: NotesTabTypes = NotesTabTypes.BY_LESSONS

    // Listener.
    private val noteActions = object : INoteItemActions {
        override fun onClick(position: Int, noteId: String) {
            if (noteViewModel.selectionModeState.value == true) {
                noteViewModel.clickByItem(noteId)
            } else {
                openNoteEditor(noteId)
            }
        }

        override fun longPress(position: Int, noteId: String) {
            noteViewModel.clickByItem(noteId)
            activity?.invalidateOptionsMenu()
        }
    }

    // Observers.
    private val notesObserver = Observer<List<NoteItem>?> { adapter.updateItems(it) }

    private val noteListIsEmptyObserver = Observer<Boolean> { viewBinding.listIsEmpty.isVisible = it }

    private val selectionModeStateObserver = Observer<Boolean> { adapter.updateSelectionMode(it) }

    override fun injectToComponent() = NotesComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            tabTypes = NotesTabTypes.values()[getInt(NOTE_TAB_TYPE)]
        }
        noteViewModel = createSharedViewModelWithParentFragment(viewModelFactory)
        adapter = NotesRecyclerViewAdapter(requireContext(), noteActions).also { it.setHasStableIds(true) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_note_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.adapter = adapter
        noteViewModel.getIsEmptyNotesLivedata(tabTypes).observe(viewLifecycleOwner, noteListIsEmptyObserver)
        noteViewModel.getNotesLiveData(tabTypes).observe(viewLifecycleOwner, notesObserver)
        noteViewModel.selectionModeState.observe(viewLifecycleOwner, selectionModeStateObserver)
    }

    /**
     * Open note editor.
     *
     * @param noteId Note id
     */
    private fun openNoteEditor(noteId: String) = tabRouter?.navigateTo(
        PolytechFragmentScreen {
            NoteEditorFragment.newInstance(noteId)
        }
    )

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }

    companion object {
        /**
         * New instance.
         *
         * @param notesTabTypes Notes tab types
         * @return [NoteListFragment] with initialized arguments
         */
        internal fun newInstance(notesTabTypes: NotesTabTypes) = NoteListFragment().apply {
            arguments = Bundle().apply {
                putInt(NOTE_TAB_TYPE, notesTabTypes.ordinal)
            }
        }
    }
}
