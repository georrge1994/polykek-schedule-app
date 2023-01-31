package com.android.feature.feedback.api

import com.android.feature.feedback.models.Feedback
import com.android.shared.code.utils.general.toCalendar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

private const val CALLBACK_MESSAGES = "CALLBACK_MESSAGES"

/**
 * This repository provides communication with firebase cloud. I don't have access to Polytech-server and store users`
 * feedback in cloud.
 *
 * @constructor Create empty constructor for firebase repository
 */
internal class FeedbackApiRepository @Inject constructor(private val firebaseApiRepository: FirebaseApiRepository) {
    /**
     * Push feedback.
     *
     * @param feedback Feedback
     * @return Is pushing successful
     */
    internal suspend fun pushFeedback(feedback: Feedback): Boolean = with(feedback) {
        withContext(Dispatchers.IO) {
            val timestamp: Long = date.time
            val year = date.toCalendar().get(GregorianCalendar.YEAR)
            firebaseApiRepository.updateChildren(
                mapOf("$CALLBACK_MESSAGES/${feedback.type}/${year}/${timestamp}" to feedback.toMap())
            )
        }
    }

    /**
     * Get firebase token.
     *
     * @return Firebase messaging token
     */
    internal suspend fun getFirebaseToken(): String = FirebaseMessaging.getInstance().token.await()
}