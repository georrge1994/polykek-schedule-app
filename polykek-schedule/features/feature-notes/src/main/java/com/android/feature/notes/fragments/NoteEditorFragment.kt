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
import com.android.feature.notes.viewModels.NoteEditorViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.syntaxSugar.hideSoftwareKeyboard

private const val SELECTED_ITEM = "SELECTED_ITEM"
private const val NOTE_ID = "NOTE_ID"
private const val LESSON = "LESSON"

/**
 * Note editor fragment.
 *
 * @constructor Create empty constructor for note editor fragment
 */
internal class NoteEditorFragment : CameraFragment() {
    private val viewBinding by viewBinding(FragmentNoteEditorBinding::bind)
    private lateinit var noteViewModel: NoteEditorViewModel

    private val focusListener = View.OnFocusChangeListener { view, _ ->
        (view as AppCompatEditText).saveContent()
    }

    private val noteLiveDataObserver = Observer<Note> { note ->
        viewBinding.noteHeader.setText(note.header)
        viewBinding.noteBody.setText(note.body)
        viewBinding.toolbarLayout.toolbar.updateToolbar(note.name, true)
    }

    override fun getComponent(): IModuleComponent = NotesComponentHolder.getComponent()

    override fun injectToComponent() = NotesComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = createViewModel(viewModelFactory)
        arguments?.apply {
            noteViewModel.init(getString(NOTE_ID), getString(LESSON), getString(SELECTED_ITEM))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_note_editor, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.noteHeader.onFocusChangeListener = focusListener
        viewBinding.noteBody.onFocusChangeListener = focusListener
        noteViewModel.noteLiveData.observe(viewLifecycleOwner, noteLiveDataObserver)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.note_editor, menu)

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.removeNote) {
            noteViewModel.deleteNote()
            tabRouter?.exit()
        }
        return super.onMenuItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        viewBinding.noteHeader.hideSoftwareKeyboard()
        viewBinding.noteBody.hideSoftwareKeyboard()
        noteViewModel.updateNote()
    }

    /**
     * Save content.
     *
     * @receiver [AppCompatEditText]
     */
    private fun AppCompatEditText.saveContent() = when (id) {
        R.id.noteHeader -> noteViewModel.saveHeader(editableText.toString())
        R.id.noteBody -> noteViewModel.saveBody(editableText.toString())
        else -> {}
    }

    companion object {
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
                putString(LESSON, title)
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