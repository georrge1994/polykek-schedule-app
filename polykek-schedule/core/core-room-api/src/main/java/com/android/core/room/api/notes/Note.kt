package com.android.core.room.api.notes

/**
 * Note.
 *
 * @property id Note id
 * @property name Name (changeable)
 * @property header Header (changeable)
 * @property body Description (changeable)
 * @constructor Create [Note]
 */
data class Note(
    val id: String,
    var name: String,
    var header: String,
    var body: String
) {
    /**
     * Is blank note.
     *
     * @return Condition result
     */
    fun isBlank() = header.isBlank() && body.isBlank()
}