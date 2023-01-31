package com.android.feature.feedback.api

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Firebase api repository. Not testable class, because firebase doesn't provide normal ways for it.
 *
 * @constructor Create empty constructor for firebase api repository
 */
internal class FirebaseApiRepository @Inject constructor() {
    /**
     * Update children.
     *
     * @param values Values
     * @return Is successful
     */
    internal suspend fun updateChildren(values: Map<String, Any>): Boolean = withContext(Dispatchers.IO) {
        val request = Firebase.database.reference.updateChildren(values)
        request.await()
        request.isSuccessful
    }
}