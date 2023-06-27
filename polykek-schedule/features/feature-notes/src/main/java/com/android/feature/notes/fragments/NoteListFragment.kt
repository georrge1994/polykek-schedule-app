package com.android.feature.notes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.NavigationFragment
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.notes.R
import com.android.feature.notes.adapters.recycler.INoteItemActions
import com.android.feature.notes.adapters.recycler.NotesRecyclerViewAdapter
import com.android.feature.notes.dagger.NotesComponentHolder
import com.android.feature.notes.databinding.FragmentNoteListBinding
import com.android.feature.notes.models.NotesTabType
import com.android.feature.notes.mvi.NotesAction
import com.android.feature.notes.mvi.NotesIntent
import com.android.feature.notes.mvi.NotesState
import com.android.feature.notes.viewModels.NotesViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createSharedViewModelWithParentFragment

private const val NOTE_TAB_TYPE = "NOTE_TAB_TYPE"

/**
 * Note list fragment contains note items and works in one of modes [NotesTabType].
 *
 * @constructor Create empty constructor for note list fragment
 */
internal class NoteListFragment : NavigationFragment<NotesIntent, NotesState, NotesAction, NotesViewModel>() {
    private val viewBinding by viewBinding(FragmentNoteListBinding::bind)
    private lateinit var adapter: NotesRecyclerViewAdapter
    private var tabType: NotesTabType = NotesTabType.BY_LESSONS

    // Listener.
    private val noteActions = object : INoteItemActions {
        override fun onClick(position: Int, noteId: String) {
            NotesIntent.ClickByNote(noteId, tabType).dispatchIntent()
        }

        override fun longPress(position: Int, noteId: String) {
            NotesIntent.LongPressByNote(noteId).dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = NotesComponentHolder.getComponent()

    override fun injectToComponent() = NotesComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            tabType = NotesTabType.values()[getInt(NOTE_TAB_TYPE)]
        }
        viewModel = createSharedViewModelWithParentFragment(viewModelFactory)
        adapter = NotesRecyclerViewAdapter(requireContext(), noteActions).also { it.setHasStableIds(true) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_note_list, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.recyclerView.adapter = adapter
    }

    override fun invalidateUi(state: NotesState) {
        super.invalidateUi(state)
        viewBinding.listIsEmpty.isVisible = state.notes[tabType].isNullOrEmpty()
        adapter.updateItems(state.notes[tabType])
        adapter.updateSelectionMode(state.isSelectionModeEnabled)
    }

    override fun executeSingleAction(action: NotesAction) {
        super.executeSingleAction(action)
        if (action is NotesAction.OpenNoteEditor && action.tabType == tabType) {
            openNoteEditor(action.noteId)
        }
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
         * @param notesTabType Notes tab types
         * @return [NoteListFragment] with initialized arguments
         */
        internal fun newInstance(notesTabType: NotesTabType) = NoteListFragment().apply {
            arguments = Bundle().apply {
                putInt(NOTE_TAB_TYPE, notesTabType.ordinal)
            }
        }
    }
}
