package com.example.mysalon.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import com.example.mysalon.R
import com.example.mysalon.model.remote.data.getAppointments.AppointmentInfo
import com.example.mysalon.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFCMService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val aptNo = message.data["aptNo"]
        Log.e("aptNo", aptNo.toString())
        showInterviewScheduledNotificaiton(message.notification!!.title, message.notification!!.body, aptNo)

        for((k,v) in message.data) {
            Log.d("MyFCMService", "onMessageReceived: $k = $v")
        }
    }

    private fun showInterviewScheduledNotificaiton(
        title: String?,
        msg: String?,
        aptNo: String?
    ) {
        val id = Random.nextInt(0, Int.MAX_VALUE)

        val idIntent = Intent(baseContext, MainActivity::class.java).apply {
            putExtra("msg", msg)
            putExtra("aptNo", aptNo)
        }

        val pendingIntent = PendingIntent.getActivity(
            baseContext,
            id,
            idIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notification = NotificationCompat.Builder(this, "SalonAlerts").apply {
            setContentTitle(title?:"")
            setContentText(msg?:"")
            setSmallIcon(R.drawable.ic_outline_email_24)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }.build()

        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("SalonAlerts", "Salon Alerts", NotificationManager.IMPORTANCE_HIGH)
            nm.createNotificationChannel(channel)
        }

        nm.notify(id, notification)

    }

}