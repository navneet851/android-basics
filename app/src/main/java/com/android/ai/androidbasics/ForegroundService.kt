package com.android.ai.androidbasics

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ForegroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    //what we on foreground service
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            "start" -> start()
            "stop" -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        val notification = NotificationCompat.Builder(this, "101")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Foreground Service")
            .setContentText("From App Android Basics")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // This will make the notification pop up with sound and vibration
            .build()
        startForeground(101, notification)
    }


}