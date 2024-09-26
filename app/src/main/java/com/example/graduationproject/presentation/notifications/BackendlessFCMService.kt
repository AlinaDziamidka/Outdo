package com.example.graduationproject.presentation.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.graduationproject.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class BackendlessFCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            val messageJson =
                remoteMessage.data["message"] ?: "{}"
            parseAndShowNotification(messageJson)
        }
    }

    private fun parseAndShowNotification(messageJson: String) {
        val jsonObject = JSONObject(messageJson)
        val creatorName = jsonObject.getString("creatorName")
        val groupName = jsonObject.getString("groupName")
        val groupId = jsonObject.getString("groupId")
        val localizedMessage = getLocalizedNotificationText(creatorName, groupName)
        val localizedTitle = getLocalizedNotificationTitle()
        showNotification(localizedTitle, localizedMessage)
    }

    private fun getLocalizedNotificationText(creatorName: String, groupName: String): String {
            val resources = applicationContext.resources
            val resId =
                resources.getIdentifier("notification_screen_description", "string", packageName)
           return if (resId != 0) {
                resources.getString(resId, creatorName, groupName)
            } else {
                "User $creatorName added you to the $groupName group!"
            }
    }

    private fun getLocalizedNotificationTitle(): String {
        val resources = applicationContext.resources
        val resId =
            resources.getIdentifier("notification_screen_notification_title", "string", packageName)
        return if (resId != 0) {
            resources.getString(resId)
        } else {
            "You have been added to the group"
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, NotificationView::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_notification)


        val notificationBuilder = NotificationCompat.Builder(this, "default_channel_id")
            .setSmallIcon(R.drawable.ic_small_notification)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(this, R.color.color_primary))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}
