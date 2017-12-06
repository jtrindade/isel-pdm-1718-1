package pt.isel.pdm.jht.reminder02

import android.app.Application
import android.util.Log

class ReminderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.i("pdm/rem02", "Application::onCreate")
        PeriodicJobService.scheduleJob(applicationContext)
    }

}
