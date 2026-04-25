package com.resqnet

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ResQNetApplication : Application() {

    companion object {
        const val CHANNEL_ID_SOS = "sos_channel"
        const val CHANNEL_ID_MESSAGES = "messages_channel"
        const val CHANNEL_ID_EMERGENCY = "emergency_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            // SOS Channel
            val sosChannel = NotificationChannel(
                CHANNEL_ID_SOS,
                "SOS Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical SOS emergency alerts"
            }

            // Messages Channel
            val messagesChannel = NotificationChannel(
                CHANNEL_ID_MESSAGES,
                "Messages",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Incoming messages from nearby devices"
            }

            // Emergency Channel
            val emergencyChannel = NotificationChannel(
                CHANNEL_ID_EMERGENCY,
                "Emergency Mode",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Emergency mode notifications"
            }

            notificationManager.createNotificationChannel(sosChannel)
            notificationManager.createNotificationChannel(messagesChannel)
            notificationManager.createNotificationChannel(emergencyChannel)
        }
    }
}
