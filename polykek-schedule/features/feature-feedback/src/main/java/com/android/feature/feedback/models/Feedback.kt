package com.android.feature.feedback.models

import java.text.SimpleDateFormat
import java.util.*

private const val TEXT = "text"
private const val TYPE = "type"
private const val TIMESTAMP = "time"
private const val DEVICE_MODEL = "deviceModel"
private const val FMS_TOKEN = "token"
private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss:SSSZZZZZ"

/**
 * Feedback.
 *
 * @property message Text of message
 * @property type Type of message
 * @property date Time of request
 * @property deviceModel Device model
 * @property token Firebase unique token
 * @constructor Create [Feedback]
 */
internal data class Feedback(
    val message: String,
    val type: FeedbackType,
    val date: Date,
    var deviceModel: String,
    val token: String
) {
    /**
     * Convert [Feedback] to map.
     *
     * @return [Map]
     */
    internal fun toMap() = mapOf(
        TEXT to message,
        TYPE to type,
        TIMESTAMP to SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(date),
        DEVICE_MODEL to deviceModel,
        FMS_TOKEN to token
    )
}