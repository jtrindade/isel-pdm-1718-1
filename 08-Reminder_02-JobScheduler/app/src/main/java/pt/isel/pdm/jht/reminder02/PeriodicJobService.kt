package pt.isel.pdm.jht.reminder02

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

class PeriodicJobService : JobService() {

    private val uiHandler by lazy { Handler(Looper.getMainLooper()) }

    @Volatile
    var stopRequested = false

    @Volatile
    var needsReschedule = false

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i("pdm/rem02", "PeriodicJobService::onStartJob")
        Toast.makeText(this, "JOB REMINDER: PDM", Toast.LENGTH_SHORT).show()
        object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit) {
                Log.i("pdm/rem02", "PeriodicJobService::doInBackground")
                execStep {
                    toast("STARTING")
                    Thread.sleep(8000)
                }
                needsReschedule = true
                execStep {
                    toast("WORKING 1")
                    Thread.sleep(8000)
                }
                execStep {
                    toast("WORKING 2")
                    Thread.sleep(8000)
                }
                needsReschedule = false
                toast("DONE")
            }
            override fun onPostExecute(result: Unit) {
                Log.i("pdm/rem02", "PeriodicJobService::onPostExecute")
                scheduleJob(applicationContext)
                jobFinished(params, false)
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        // This method is not usually called in normal conditions.
        // It will be called whenever the system decides that the service should stop running (e.g. to free resources)
        // However we see it called at the end of the background work.
        // This is because we reprogram another job with the same id at the end of the background work.
        // When a job is reprogrammed, the system forces a stop on the current execution.
        Log.i("pdm/rem02", "PeriodicJobService::onStopJob")
        Toast.makeText(this, "STOP!!!", Toast.LENGTH_SHORT).show()
        stopRequested = true
        return needsReschedule
    }

    fun execStep(step: () -> Unit) {
        if (!stopRequested) {
            step()
        }
    }

    fun toast(message: String) {
        uiHandler.post { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
    }

    companion object {

        val PERIODIC_JOB_ID = 1

        fun scheduleJob(context: Context) {
            val jobServiceName = ComponentName(context, PeriodicJobService::class.java)

            val jobInfo = JobInfo.Builder(PERIODIC_JOB_ID, jobServiceName)
                    .setMinimumLatency(16000)
                    .setOverrideDeadline(24000)
                    .build()

            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)
        }

    }
}