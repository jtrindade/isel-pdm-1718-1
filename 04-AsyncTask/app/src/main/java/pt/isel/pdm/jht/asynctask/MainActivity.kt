package pt.isel.pdm.jht.asynctask

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val txtBox by lazy { findViewById(R.id.txtBox) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onGo(view: View?) {
        Toast.makeText(this, "Let's go!", Toast.LENGTH_SHORT).show()
        object : AsyncTask<Void, Int, String>() {
            override fun doInBackground(vararg params: Void?): String {
                (0 .. 10).forEach {
                    publishProgress(it * 10)
                    Thread.sleep(1000)
                }
                return "DONE"
            }

            override fun onProgressUpdate(vararg values: Int?) {
                txtBox.appendLine("${values[0]}")
            }

            override fun onPostExecute(result: String?) {
                txtBox.appendLine(result?:"---")
            }
        }.execute()
    }
}

fun TextView.appendLine(str: String) {
    this.setText("${this.getText()}\n$str")
}