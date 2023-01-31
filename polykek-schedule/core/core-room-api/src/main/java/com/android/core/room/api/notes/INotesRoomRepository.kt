package com.android.core.room.api.notes

import kotlinx.coroutines.flow.Flow

/**
 * Contract for notes room repository.
 */
interface INotesRoomRepository {
    /**
     * Get notes which ids contain [noteIdBit] and any fields contain [keyWord].
     *
     * @param noteIdBit Note id bit
     * @param keyWord Key word
     * @return Notes
     */
    suspend fun getNotesWhichContains(noteIdBit: String, keyWord: String): List<Note>

    /**
     * Get all note ids.
     *
     * @return Notes` ids
     */
    suspend fun getNoteIds(): List<String>

    /**
     * Get note ids flow.
     *
     * @return Flow with notes` ids
     */
    fun getNoteIdsFlow(): Flow<List<String>?>

    /**
     * Get note by id.
     *
     * @param noteId Note id
     * @return [Note] or null
     */
    suspend fun getNoteById(noteId: String): Note?

    /**
     * Insert.
     *
     * @param note Note
     */
    suspend fun insert(note: Note)

    /**
     * Delete notes by ids.
     *
     * @param noteIds Note ids
     */
    suspend fun deleteNotesByIds(noteIds: Collection<String>)

    /**
     * Delete note by id.
     *
     * @param noteId Note id
     */
    suspend fun deleteNoteById(noteId: String): Int
}