package com.android.common.models.schedule

import com.android.common.models.professors.Professor

/**
 * Lesson.
 *
 * @property time Interval of lesson
 * @property title Name of lesson
 * @property address Place of lesson
 * @property buildingNames Building name
 * @property typeLesson Type of lesson
 * @property teacherNames Professor's name
 * @property withNotes True, if lesson has an note
 * @property noteId Note id (event note doesn't exit Id will be prepared)
 * @property groupId Group id
 * @property professors Professors (usually this he is alone)
 * @constructor Create [Lesson]
 */
data class Lesson(
    var time: String,
    var title: String,
    var address: String,
    var buildingNames: String?,
    var typeLesson: String,
    var teacherNames: String,
    var withNotes: Boolean,
    var noteId: String,
    var groupId: Int,
    val professors: List<Professor?>
)