package pt.isel.pdm.jht.reminder01

import android.app.IntentService
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class PeriodicService : IntentService("Periodic") {

    private val uiHandler by lazy { Handler(Looper.getMainLooper()) }

    override fun onHandleIntent(intent: Intent?) {
        startForeground(
                1, // cannot be 0
                notificationBuilder(applicationContext, SERVICES_CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.star_on)
                        .setContentTitle("Reminder in background")
                        .setContentText("This app wakes up periodically")
                        .setOngoing(true)
                        .build()
        )

        toast("STARTING")
        Thread.sleep(8000)
        toast("WORKING 1")
        Thread.sleep(8000)
        toast("WORKING 2")
        Thread.sleep(8000)
        toast("DONE")

        stopForeground(true)
    }

    fun toast(message: String) {
        uiHandler.post { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
    }

    fun notificationBuilder(context: Context, channelId: String) =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(context, channelId)
            } else {
                Notification.Builder(context)
            }
}

