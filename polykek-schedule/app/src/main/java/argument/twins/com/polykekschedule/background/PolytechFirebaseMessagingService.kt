package argument.twins.com.polykekschedule.background

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import argument.twins.com.polykekschedule.R
import argument.twins.com.polykekschedule.activity.MainActivity
import com.android.shared.code.utils.general.SharedPreferenceUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import javax.inject.Inject

private const val SPIRIT_NOTIFICATION_CHANNEL_ID = "SPIRIT_NOTIFICATION_CHANNEL_ID"
private const val SPIRIT_NOTIFICATION_ID = 0

/**
 * Polytech firebase messaging service.
 *
 * @constructor Create empty constructor for polytech firebase messaging service
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PolytechFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.showNotification()
    }

    /**
     * Show notification.
     *
     * @receiver [RemoteMessage.Notification]
     */
    private fun RemoteMessage.Notification.showNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !sharedPreferenceUtils.contains(SPIRIT_NOTIFICATION_CHANNEL_ID)) {
            notificationManager.createSpiritNotificationChannel()
            sharedPreferenceUtils.add(SPIRIT_NOTIFICATION_CHANNEL_ID, true)
        }
        notificationManager.notify(SPIRIT_NOTIFICATION_ID, getNotification())
    }

    /**
     * Create spirit notification channel.
     *
     * @receiver [NotificationManager]
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun NotificationManager.createSpiritNotificationChannel() = NotificationChannel(
        SPIRIT_NOTIFICATION_CHANNEL_ID,
        getString(R.string.fcm_notification_channel_name),
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = getString(R.string.fcm_notification_channel_description)
        lightColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        enableVibration(false)
        enableLights(true)
        this@createSpiritNotificationChannel.createNotificationChannel(this@apply)
    }

    /**
     * Get notification.
     *
     * @receiver [RemoteMessage.Notification]
     * @return [Notification]
     */
    private fun RemoteMessage.Notification.getNotification(): Notification =
        NotificationCompat.Builder(applicationContext, SPIRIT_NOTIFICATION_CHANNEL_ID)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_message_24))
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntent())
            .build()

    /**
     * Get pending intent.
     *
     * @return [PendingIntent] for opening [MainActivity]
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(this, SPIRIT_NOTIFICATION_ID, intent, flags)
    }
}