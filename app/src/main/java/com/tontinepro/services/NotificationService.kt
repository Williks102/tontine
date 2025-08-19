package com.tontinepro.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tontinepro.R
import com.tontinepro.TontineApplication
import com.tontinepro.data.models.enums.NotificationType
import com.tontinepro.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Extraire les données de la notification
        val title = remoteMessage.notification?.title ?: "TONTINE PRO"
        val body = remoteMessage.notification?.body ?: ""
        val type = remoteMessage.data["type"]?.let {
            NotificationType.valueOf(it)
        } ?: NotificationType.SYSTEM_UPDATE

        // Afficher la notification
        showNotification(title, body, type)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Envoyer le nouveau token à votre serveur
        sendTokenToServer(token)
    }

    private fun showNotification(title: String, body: String, type: NotificationType) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = when (type) {
            NotificationType.PAYMENT_REMINDER,
            NotificationType.PAYMENT_RECEIVED,
            NotificationType.TURN_NOTIFICATION -> TontineApplication.PAYMENT_CHANNEL_ID
            NotificationType.PARRAINAGE_BONUS -> TontineApplication.PARRAINAGE_CHANNEL_ID
            NotificationType.FETE_AVAILABLE -> TontineApplication.FETE_CHANNEL_ID
            else -> TontineApplication.SYSTEM_CHANNEL_ID
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(if (type.isHighPriority) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun sendTokenToServer(token: String) {
        // Implémenter l'envoi du token FCM à votre serveur
        // pour pouvoir envoyer des notifications push
    }
}