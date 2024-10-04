package com.android.shared.code.utils.email

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject

private const val MAIL_TO = "mailto:"

/**
 * Open email chooser use case.
 */
class OpenEmailChooserUseCase @Inject constructor() : IUseCase {
    /**
     * Open email chooser.
     *
     * @param context Context
     * @param email String
     * @param subject String
     * @param chooserMessage String
     */
    fun openEmailChooser(context: Context, email: String, subject: String, chooserMessage: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(MAIL_TO)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        context.startActivity(Intent.createChooser(emailIntent, chooserMessage))
    }
}