package argument.twins.com.polykekschedule.room.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

internal const val NOTES = "notes"

internal const val NOTES_ID = "id"
internal const val NOTES_NAME = "name"
internal const val NOTES_HEADER = "title"
internal const val NOTES_BODY = "body"

/**
 * Note.
 *
 * @property id Note id
 * @property name Name (changeable)
 * @property header Header (changeable)
 * @property body Description (changeable)
 * @constructor Create [NoteEntity]
 */
@Entity(tableName = NOTES)
data class NoteEntity(
    @PrimaryKey
    @ColumnInfo(name = NOTES_ID)        val id: String,
    @ColumnInfo(name = NOTES_NAME)      var name: String,
    @ColumnInfo(name = NOTES_HEADER)    var header: String,
    @ColumnInfo(name = NOTES_BODY)      var body: String
)