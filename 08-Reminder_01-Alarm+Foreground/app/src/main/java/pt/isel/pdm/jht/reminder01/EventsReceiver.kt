package pt.isel.pdm.jht.reminder01

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast

class EventsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Toast.makeText(context, "REMINDER BOOT", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "REMINDER: PDM", Toast.LENGTH_SHORT).show()
            startService(context, Intent(context.applicationContext, PeriodicService::class.java))
        }
    }

    private fun startService(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
