package pt.isel.pdm.jht.reminder01

import android.annotation.TargetApi
import android.app.*
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.support.annotation.RequiresApi

val SERVICES_CHANNEL_ID = "services.channel"

class ReminderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        setupPeriodicAlarm()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannel()
        }
    }

    private fun setupPeriodicAlarm() {

        val alarmIntent = Intent(this, EventsReceiver::class.java)

        if (PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) == null) {
            val pendingAlarmIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)

            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 10000,
                    60000,
                    pendingAlarmIntent
            )
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun setupNotificationChannel() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(SERVICES_CHANNEL_ID) == null) {
            val notificationChannel = NotificationChannel(SERVICES_CHANNEL_ID, "Reminder Services Channel", NotificationManager.IMPORTANCE_MIN)
            notificationChannel.enableLights(false)
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
