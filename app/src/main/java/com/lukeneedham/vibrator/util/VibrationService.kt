package com.lukeneedham.vibrator.util

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Vibrator
import androidx.core.app.NotificationCompat

class VibrationService : Service() {

    private val handler = Handler()
    private lateinit var vibrator: Vibrator
    private val VIBRATION_INTERVAL = 4 * 60 * 1000L // 4 minutes in milliseconds
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "VibrationServiceChannel"

    private val vibrateTask = object : Runnable {
        override fun run() {
            vibrator.vibrate(1000) // Vibrate for 1 second
            handler.postDelayed(this, VIBRATION_INTERVAL)
        }
    }

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        startForegroundService()
        handler.post(vibrateTask) // Start periodic vibration
    }

    private fun startForegroundService() {
        // Create notification channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Vibration Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        // Build and start the notification
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Vibration Service")
            .setContentText("Vibrating every 4 minutes...")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // Restart the service if it’s killed
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(vibrateTask) // Stop vibrations when destroyed
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // Not a bound service
    }
}
