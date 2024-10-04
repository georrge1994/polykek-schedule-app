package com.android.feature.notes.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.room.api.notes.Note
import com.android.feature.notes.R
import com.android.feature.notes.dagger.NotesComponentHolder
import com.android.feature.notes.databinding.FragmentNoteEditorBinding
import com.android.feature.notes.mvi.NoteEditorAction
import com.android.feature.notes.mvi.NoteEditorIntent
import com.android.feature.notes.mvi.NoteEditorState
import com.android.feature.notes.viewModels.NoteEditorViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.syntaxSugar.hideSoftwareKeyboard

private const val SELECTED_ITEM = "SELECTED_ITEM"
private const val NOTE_ID = "NOTE_ID"
private const val TITLE = "TITLE"

/**
 * Note editor fragment.
 *
 * @constructor Create empty constructor for note editor fragment
 */
internal class NoteEditorFragment :
    CameraFragment<NoteEditorIntent, NoteEditorState, NoteEditorAction, NoteEditorViewModel>() {
    private val viewBinding by viewBinding(FragmentNoteEditorBinding::bind)

    private val focusListener = View.OnFocusChangeListener { view, _ -> (view as AppCompatEditText).saveContent() }

    override fun getComponent(): IModuleComponent = NotesComponentHolder.getComponent()

    override fun injectToComponent() = NotesComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
        NoteEditorIntent.InitContent(
            arguments?.getString(NOTE_ID),
            arguments?.getString(TITLE),
            arguments?.getString(SELECTED_ITEM)
        ).dispatchIntent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_note_editor, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.noteHeader.onFocusChangeListener = focusListener
        viewBinding.noteBody.onFocusChangeListener = focusListener
    }

    override fun invalidateUi(state: NoteEditorState) {
        super.invalidateUi(state)
        state.note?.apply {
            viewBinding.noteHeader.setText(header)
            viewBinding.noteBody.setText(body)
            viewBinding.toolbarLayout.toolbar.updateToolbar(name, true)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.note_editor, menu)

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.removeNote) {
            NoteEditorIntent.DeleteNote.dispatchIntent()
            return true
        }
        return super.onMenuItemSelected(item)
    }

    override fun executeSingleAction(action: NoteEditorAction) {
        super.executeSingleAction(action)
        if (action is NoteEditorAction.Exit) {
            tabRouter?.exit()
        }
    }

    override fun onPause() {
        super.onPause()
        viewBinding.noteHeader.hideSoftwareKeyboard()
        viewBinding.noteBody.hideSoftwareKeyboard()
        NoteEditorIntent.UpdateNote.dispatchIntent()
    }

    /**
     * Save content.
     *
     * @receiver [AppCompatEditText]
     */
    private fun AppCompatEditText.saveContent() {
        when (id) {
            R.id.noteHeader -> NoteEditorIntent.SaveHeader(editableText.toString()).dispatchIntent()
            R.id.noteBody -> NoteEditorIntent.SaveBody(editableText.toString()).dispatchIntent()
            else -> {}
        }
    }

    internal companion object {
        /**
         * Get instance with [noteId] and [title].
         *
         * @param noteId Note id
         * @param title Title
         * @return [Bundle]
         */
        internal fun newInstance(noteId: String, title: String): NoteEditorFragment = NoteEditorFragment().apply {
            arguments = Bundle().apply {
                putString(NOTE_ID, noteId)
                putString(TITLE, title)
            }
        }

        /**
         * Get instance with [selectedItem].
         *
         * @param selectedItem Selected item
         * @return [Bundle]
         */
        internal fun newInstance(selectedItem: Int?): NoteEditorFragment = NoteEditorFragment().apply {
            arguments = Bundle().apply {
                putString(SELECTED_ITEM, selectedItem.toString())
            }
        }

        /**
         * Get instance with [noteId].
         *
         * @param noteId Note id
         * @return [Bundle]
         */
        internal fun newInstance(noteId: String): NoteEditorFragment = NoteEditorFragment().apply {
            arguments = Bundle().apply {
                putString(NOTE_ID, noteId)
            }
        }
    }
}