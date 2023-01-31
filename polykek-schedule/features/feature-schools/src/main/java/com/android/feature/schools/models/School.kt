package com.android.feature.schools.models

/**
 * School.
 *
 * @property id School id
 * @property name School name
 * @property abbr Abbreviation of school name
 * @constructor Create [School]
 */
internal data class School(
    var id: String,
    var name: String,
    var abbr: String
)