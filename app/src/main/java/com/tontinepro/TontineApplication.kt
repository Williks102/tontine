// TontineApplication.kt
package com.tontinepro

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import co.paystack.android.PaystackSdk
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Classe Application principale pour TONTINE PRO
 * Configurée avec Hilt pour l'injection de dépendances
 */
@HiltAndroidApp
class TontineApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // Initialiser Firebase
        initializeFirebase()

        // Configurer Paystack
        initializePaystack()

        // Créer les canaux de notifications
        createNotificationChannels()

        // Initialiser WorkManager
        initializeWorkManager()

        // Configuration globale
        setupGlobalConfiguration()
    }

    /**
     * Initialise Firebase pour les notifications push et analytics
     */
    private fun initializeFirebase() {
        try {
            FirebaseApp.initializeApp(this)
            // Log pour debug
            android.util.Log.d("TontineApp", "Firebase initialisé avec succès")
        } catch (e: Exception) {
            android.util.Log.e("TontineApp", "Erreur lors de l'initialisation Firebase", e)
        }
    }

    /**
     * Configure Paystack SDK pour les paiements
     */
    private fun initializePaystack() {
        try {
            PaystackSdk.initialize(applicationContext)
            android.util.Log.d("TontineApp", "Paystack initialisé avec succès")
        } catch (e: Exception) {
            android.util.Log.e("TontineApp", "Erreur lors de l'initialisation Paystack", e)
        }
    }

    /**
     * Crée les canaux de notifications pour Android 8.0+
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Canal pour les notifications de paiement (priorité haute)
            val paymentChannel = NotificationChannel(
                PAYMENT_CHANNEL_ID,
                "Notifications de paiement",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications concernant vos paiements et réceptions"
                enableVibration(true)
                enableLights(true)
                setShowBadge(true)
            }

            // Canal pour les rappels (priorité normale)
            val reminderChannel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                "Rappels de paiement",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Rappels pour effectuer vos paiements"
                enableVibration(true)
            }

            // Canal pour les notifications de parrainage (priorité normale)
            val parrainageChannel = NotificationChannel(
                PARRAINAGE_CHANNEL_ID,
                "Notifications de parrainage",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications concernant vos parrainages et bonus"
            }

            // Canal pour les événements fête (priorité normale)
            val feteChannel = NotificationChannel(
                FETE_CHANNEL_ID,
                "Événements Fête",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications pour les événements fête"
            }

            // Canal pour les mises à jour système (priorité basse)
            val systemChannel = NotificationChannel(
                SYSTEM_CHANNEL_ID,
                "Mises à jour système",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Mises à jour et informations système"
                setShowBadge(false)
            }

            // Canal pour le chat support (priorité haute)
            val supportChannel = NotificationChannel(
                SUPPORT_CHANNEL_ID,
                "Chat Support",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Messages du support client"
                enableVibration(true)
            }

            // Enregistrer tous les canaux
            notificationManager.createNotificationChannels(
                listOf(
                    paymentChannel,
                    reminderChannel,
                    parrainageChannel,
                    feteChannel,
                    systemChannel,
                    supportChannel
                )
            )

            android.util.Log.d("TontineApp", "Canaux de notifications créés")
        }
    }

    /**
     * Initialise WorkManager pour les tâches en arrière-plan
     */
    private fun initializeWorkManager() {
        try {
            // WorkManager sera automatiquement configuré via getWorkManagerConfiguration()
            android.util.Log.d("TontineApp", "WorkManager configuré")
        } catch (e: Exception) {
            android.util.Log.e("TontineApp", "Erreur lors de la configuration WorkManager", e)
        }
    }

    /**
     * Configuration globale de l'application
     */
    private fun setupGlobalConfiguration() {
        // Configuration pour les logs en mode debug
        if (BuildConfig.DEBUG) {
            // Activer les logs détaillés en mode debug
            android.util.Log.d("TontineApp", "Mode DEBUG activé")
        }

        // Configuration globale des timeouts réseau
        System.setProperty("http.keepAlive", "false")
    }

    /**
     * Configuration pour WorkManager avec Hilt
     */
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(
                if (BuildConfig.DEBUG) android.util.Log.DEBUG else android.util.Log.INFO
            )
            .build()
    }

    companion object {
        // IDs des canaux de notifications
        const val PAYMENT_CHANNEL_ID = "tontine_payment_channel"
        const val REMINDER_CHANNEL_ID = "tontine_reminder_channel"
        const val PARRAINAGE_CHANNEL_ID = "tontine_parrainage_channel"
        const val FETE_CHANNEL_ID = "tontine_fete_channel"
        const val SYSTEM_CHANNEL_ID = "tontine_system_channel"
        const val SUPPORT_CHANNEL_ID = "tontine_support_channel"

        // Tags pour WorkManager
        const val PAYMENT_REMINDER_WORK = "payment_reminder_work"
        const val CYCLE_UPDATE_WORK = "cycle_update_work"
        const val SYNC_DATA_WORK = "sync_data_work"
    }
}