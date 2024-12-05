package com.esilv.hiphiphip.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters


class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        showNotification("Dystopian Alert", "The system is watching...")
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {

        Log.d("Worker", "Notification send")

        val channelId = "dystopian_alerts"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android 8.0+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Dystopian Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for dystopian system alerts"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Build and display the notification
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert) // Replace with custom drawable if needed
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }


}
