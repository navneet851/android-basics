package com.android.ai.androidbasics

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SyncingData(context: Context, workerParams: WorkerParameters)
    : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO){
            //to sign data for 10 seconds
            val data = inputData.getString("sync_data")
            var counter = 5
            do {
                syncNotification(counter, data?: "No data")
                delay(500)
                counter--
            }while (counter > 0)
        }
        // Clear the notification when the counter reaches 0
        with(NotificationManagerCompat.from(applicationContext)) {
            cancel(1)
        }

        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun syncNotification(counter: Int, body: String){
        val notification = NotificationCompat.Builder(applicationContext, "102")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Syncing... $counter")
            .setContentText(body)
            .setVibrate(null)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setDefaults(0) // This will make the notification off sound and vibration
            .build()

        with(NotificationManagerCompat.from(applicationContext)){
            notify(1, notification)
        }
    }
}