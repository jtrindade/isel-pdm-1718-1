package pt.isel.pdm.jht.reminder02

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class EventsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("pdm/rem02", "EventsReceiver::onReceive")
        Toast.makeText(context, "JOB REMINDER BOOT", Toast.LENGTH_SHORT).show()
    }
}
