package com.example.graduationproject.data.remote.api
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.os.Build
//import android.Manifest
//import android.content.pm.PackageManager
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//class MyFirebaseMessagingService : FirebaseMessagingService() {
//
//    companion object {
//        private const val CHANNEL_ID = "my_channel_id"
//        private const val REQUEST_CODE_POST_NOTIFICATIONS = 1001
//    }
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_POST_NOTIFICATIONS)
//        } else {
//            showNotification(remoteMessage)
//        }
//    }
//
//    private fun showNotification(remoteMessage: RemoteMessage) {
//        val title = remoteMessage.notification?.title
//        val message = remoteMessage.notification?.body
//
//        // Создание канала уведомлений для Android O и выше
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "My Channel"
//            val descriptionText = "Channel Description"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//
//            // Регистрация канала с системой
//            val notificationManager: NotificationManager =
//                getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // Создание уведомления с использованием NotificationCompat.Builder
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.notification_icon)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        // Показ уведомления
//        with(NotificationManagerCompat.from(this)) {
//            notify(0, builder.build())
//        }
//    }
//}