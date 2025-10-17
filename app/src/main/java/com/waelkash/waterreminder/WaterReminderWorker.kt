package com.waelkash.waterreminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.*

class WaterReminderWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        showNotification(applicationContext)
        return Result.success()
    }

    private fun showNotification(ctx: Context) {
        val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Water reminders",
            NotificationManager.IMPORTANCE_HIGH
        )
        nm.createNotificationChannel(channel)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

val notif = NotificationCompat.Builder(ctx, CHANNEL_ID)
    .setSmallIcon(android.R.drawable.ic_dialog_info)
    .setContentTitle(ctx.getString(R.string.reminder_title))
    .setContentText(ctx.getString(R.string.reminder_text))
    .setAutoCancel(true)
    .setSound(alarmSound)
    .build()

        nm.notify(NOTIFICATION_ID, notif)
    }

    companion object {
        private const val CHANNEL_ID = "water_channel"
        private const val NOTIFICATION_ID = 1001

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<WaterReminderWorker>(
                30, java.util.concurrent.TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "water_reminder",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    request
                )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork("water_reminder")
        }
    }
}
